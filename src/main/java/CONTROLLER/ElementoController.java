package controller;

import java.util.ArrayList;
import model.dao.ElementoDAO;
import model.dao.ReporteDAO;
import model.Elemento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import model.Reporte;
import model.dao.AmbienteDAO;
import model.dao.EstadoDAO;
import model.dao.InventarioDAO;
import model.dao.TipoElementoDAO;

/**
 * Controlador REST para gestionar operaciones relacionadas con los elementos.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar elementos.
 *  
 * 
 * Rutas disponibles:
 * - GET /elementos
 * - GET /elementos/{id}
 * - GET /elementos/placa/{placa}
 * - GET /elementos/inventario/{id}
 * - POST /elementos
 * - PUT /elementos/{id}
 * - PUT /elementos/{id}/estado/{estado}
 * - DELETE /elementos/{id}
 * 
 * Autor: Yariangel Aray
 */
@Path("/elementos")
public class ElementoController {

    private final ElementoDAO dao;     // DAO para acceder a datos de elementos
    private final ReporteDAO reporteDao; // DAO para consultar reportes asociados
    private final TipoElementoDAO tipoElementoDao;
    private final EstadoDAO estadoDao;
    private final AmbienteDAO ambienteDao;
    private final InventarioDAO inventarioDao;


    public ElementoController() {
        // Se inicializan las instancias DAO
        dao = new ElementoDAO();
        reporteDao = new ReporteDAO();
        this.tipoElementoDao = new TipoElementoDAO();
        this.estadoDao = new EstadoDAO();
        this.ambienteDao = new AmbienteDAO();
        this.inventarioDao = new InventarioDAO();
    }

    /**
     * Obtiene todos los elementos registrados en el sistema.
     *
     * @return Lista de elementos o mensaje de error si ocurre una excepción.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Consulta todos los elementos en base de datos
            List<Elemento> elementos = dao.getAll();

            // Si la lista está vacía, se retorna error 404
            if (elementos.isEmpty()) {
                return ResponseProvider.error("No se encontraron elementos", 404);
            }

            // Si hay resultados, se retornan correctamente
            return ResponseProvider.success(elementos, "Elementos obtenidos correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene todos los elementos que pertenecen a un inventario específico.
     *
     * @param idInventario ID del inventario.
     * @return Lista de elementos del inventario.
     */
    @GET
    @Path("/inventario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorInventario(@PathParam("id") int idInventario) {
        try {
            // Consulta los elementos por ID de inventario
            List<Elemento> elementos = dao.getAllByIdInventario(idInventario);

            // Si no se encontraron elementos
            if (elementos == null || elementos.isEmpty()) {
                return ResponseProvider.error("No se encontraron elementos para el inventario especificado", 204);
            }

            // Retorna lista de elementos
            return ResponseProvider.success(elementos, "Elementos del inventario obtenidos correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un elemento por su ID.
     *
     * @param id ID del elemento.
     * @return Elemento encontrado o error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerElemento(@PathParam("id") int id) {
        try {
            // Consulta el elemento por ID
            Elemento elemento = dao.getById(id);

            // Verifica si fue encontrado
            if (elemento == null) {
                return ResponseProvider.error("Elemento no encontrado", 404);
            }

            // Retorna el elemento encontrado
            return ResponseProvider.success(elemento, "Elemento obtenido correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un elemento por su número de placa.
     *
     * @param placa Número de placa del elemento.
     * @return Elemento encontrado o error si no existe.
     */
    @GET
    @Path("/placa/{placa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerElementoPorPlaca(@PathParam("placa") long placa) {
        try {
            // Consulta el elemento por número de placa
            Elemento elemento = dao.getByPlaca(placa).get(0);

            // Verifica si existe
            if (elemento == null) {
                return ResponseProvider.error("Elemento no encontrado", 404);
            }

            return ResponseProvider.success(elemento, "Elemento obtenido correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo elemento en el sistema.
     *
     * @param elemento Objeto recibido con los datos del elemento.
     * @return Elemento creado o mensaje de error si falla.
     */
    @POST
    @ValidarCampos(entidad = "elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearElemento(Elemento elemento) {
        try {
            // Verifica si el serial ya está registrado
            List<Elemento> elementosSerial = dao.getBySerial(elemento.getSerial());
            if (!elementosSerial.isEmpty()) {
                return ResponseProvider.error("Ya existe un elemento con este serial registrado", 409);
            }

            // Verifica si la placa ya está registrada
            List<Elemento> elementosPlaca = dao.getByPlaca(elemento.getPlaca());
            if (!elementosPlaca.isEmpty()) {
                return ResponseProvider.error("Ya existe un elemento con esta placa registrada", 409);
            }
            
            // Validar existencia de tipo_elemento
            if (tipoElementoDao.getById(elemento.getTipo_elemento_id()) == null) {
                return ResponseProvider.error("El tipo de elemento especificado no existe.", 404);
            }

            // Validar existencia de estado
            if (estadoDao.getById(elemento.getEstado_id()) == null) {
                return ResponseProvider.error("El estado especificado no existe.", 404);
            }

            // Validar existencia de inventario
            if (inventarioDao.getById(elemento.getInventario_id()) == null) {
                return ResponseProvider.error("El inventario especificado no existe.", 404);
            }

            // Validar existencia de ambiente solo si viene un ID (no es null)
            if (elemento.getAmbiente_id() != 0 && ambienteDao.getById(elemento.getAmbiente_id()) == null) {
                return ResponseProvider.error("El ambiente especificado no existe.", 404);
            }

            // Intenta crear el nuevo elemento
            Elemento creado = dao.create(elemento);
            if (creado != null) {
                return ResponseProvider.success(creado, "Elemento creado correctamente", 201);
            }

            return ResponseProvider.error("Error al crear el elemento", 400);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un elemento existente.
     *
     * @param id       ID del elemento.
     * @param elemento Nuevos datos del elemento.
     * @return Elemento actualizado o mensaje de error si no existe.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarElemento(@PathParam("id") int id, Elemento elemento) {
        try {
            // Verifica existencia
            Elemento existente = dao.getById(id);
            if (existente == null) {
                return ResponseProvider.error("Elemento no encontrado", 404);
            }

            List<Elemento> elementos = dao.getAll();
            List<Elemento> elementosRegistrados = new ArrayList<>();

            for (Elemento elementoRegistrado : elementos)
                if (elementoRegistrado.getId()!= id) elementosRegistrados.add(elementoRegistrado);

            for (Elemento elementoRegistrado : elementosRegistrados)
                if (elementoRegistrado.getPlaca()== elemento.getPlaca()){
                    return ResponseProvider.error("Este número de placa ya fue registrado", 409);
                }

            if (elemento.getSerial() != null && !elemento.getSerial().isEmpty()) {
                for (Elemento elementoRegistrado : elementosRegistrados) {
                    if (elemento.getSerial().equals(elementoRegistrado.getSerial())) {
                        return ResponseProvider.error("Este serial ya fue registrado", 409);
                    }
                }
            }
            
            // Validar existencia de tipo_elemento
            if (tipoElementoDao.getById(elemento.getTipo_elemento_id()) == null) {
                return ResponseProvider.error("El tipo de elemento especificado no existe.", 404);
            }

            // Validar existencia de estado
            if (estadoDao.getById(elemento.getEstado_id()) == null) {
                return ResponseProvider.error("El estado especificado no existe.", 404);
            }

            // Validar existencia de inventario
            if (inventarioDao.getById(elemento.getInventario_id()) == null) {
                return ResponseProvider.error("El inventario especificado no existe.", 404);
            }

            // Validar existencia de ambiente solo si viene un ID (no es null)
            if (elemento.getAmbiente_id() != 0 && ambienteDao.getById(elemento.getAmbiente_id()) == null) {
                return ResponseProvider.error("El ambiente especificado no existe.", 404);
            }

            boolean actualizado = dao.update(id, elemento); // Intenta actualizar

            if (actualizado) {
                Elemento actualizadoElemento = dao.getById(id); // Verifica existencia
                return ResponseProvider.success(actualizadoElemento, "Elemento actualizado correctamente", 200); // Éxito
            }
            
            return ResponseProvider.error("Error al actualizar el elemento", 400); // Falla            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Cambia el estado activo de un elemento (activar o desactivar).
     *
     * @param id     ID del elemento.
     * @param estado Nuevo estado (true para activo, false para inactivo).
     * @return Resultado de la operación.
     */
    @PUT
    @Path("/{id}/estado/{estado}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstado(@PathParam("id") int id, @PathParam("estado") boolean estado) {
        try {
            // Verifica que el elemento exista
            Elemento existente = dao.getById(id);
            if (existente == null) {
                return ResponseProvider.error("Elemento no encontrado", 404);
            }

            // Intenta cambiar el estado
            boolean actualizado = dao.updateState(id, estado);

            if (actualizado) {
                String mensaje = estado
                        ? "Elemento activado correctamente"
                        : "Elemento desactivado correctamente";

                return ResponseProvider.success(null, mensaje, 200);
            }
            return ResponseProvider.error("No se pudo actualizar el estado del elemento", 400);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un elemento, siempre que no tenga reportes asociados.
     *
     * @param id ID del elemento.
     * @return Resultado de la operación.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarElemento(@PathParam("id") int id) {
        try {
            // Verifica existencia del elemento
            Elemento existente = dao.getById(id);
            if (existente == null) {
                return ResponseProvider.error("Elemento no encontrado", 404);
            }
            
            List<Reporte> reportes = reporteDao.getAllByIdElemento(id); // Obtiene reportes asociados

            if (reportes != null && !reportes.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el elemento porque tiene reportes asociados", 409); // Conflicto
            }

            // Intenta eliminar
            boolean eliminado = dao.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Elemento eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el elemento", 500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
