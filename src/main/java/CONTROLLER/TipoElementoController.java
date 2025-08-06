package controller;

import model.dao.ElementoDAO;
import model.dao.TipoElementoDAO;
import model.Elemento;
import model.TipoElemento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los tipos de elementos. 
 *
 * Rutas disponibles:
 * - GET /tipos-elementos: Listar todos los tipos de elementos.
 * - GET /tipos-elementos/inventario/{id}: Listar tipos con conteo por inventario.
 * - GET /tipos-elementos/{id}: Obtener tipo por ID.
 * - POST /tipos-elementos: Crear nuevo tipo.
 * - PUT /tipos-elementos/{id}: Actualizar tipo existente.
 * - DELETE /tipos-elementos/{id}: Eliminar tipo.
 * 
 * Este controlador también valida duplicidad de consecutivos y previene eliminación si hay elementos asociados.
 * 
 * @author Yariangel Aray
 */
@Path("/tipos-elementos") // Ruta base para este recurso
public class TipoElementoController {

    private final TipoElementoDAO tipoDAO;  // DAO de tipos de elementos
    private final ElementoDAO elementoDAO;  // DAO de elementos (para validaciones)

    /**
     * Constructor del controlador. Instancia los DAOs necesarios.
     */
    public TipoElementoController() {
        tipoDAO = new TipoElementoDAO();
        elementoDAO = new ElementoDAO();
    }

    /**
     * Obtiene todos los tipos de elementos registrados en el sistema,
     * incluyendo la cantidad de elementos asociados por tipo.
     *
     * @return Lista de tipos o error si no hay resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Obtener lista de tipos desde la base de datos
            List<TipoElemento> tipos = tipoDAO.getAll();

            // Verificar si no hay registros
            if (tipos.isEmpty()) {
                return ResponseProvider.error("No se encontraron tipos de elementos", 404);
            }

            // Agregar la cantidad de elementos por cada tipo
            for (TipoElemento tipo : tipos) {
                tipo.setCantidadElementos(elementoDAO.getAllByIdTipoElemento(tipo.getId()).size());
            }

            // Retornar lista con éxito
            return ResponseProvider.success(tipos, "Tipos de elementos obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace(); // Log del error para debugging
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene todos los tipos de elementos con la cantidad de elementos
     * que hay en un inventario específico.
     *
     * @param idInventario ID del inventario.
     * @return Lista de tipos con conteo o error si no hay resultados.
     */
    @GET
    @Path("/inventario/{idInventario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosPorInventario(@PathParam("idInventario") int idInventario) {
        try {
            // Obtener todos los tipos desde el DAO
            List<TipoElemento> tipos = tipoDAO.getAll();

            // Validar existencia
            if (tipos.isEmpty()) {
                return ResponseProvider.error("No se encontraron tipos de elementos", 404);
            }

            // Obtener todos los elementos del inventario indicado
            List<Elemento> elementosInventario = elementoDAO.getAllByIdInventario(idInventario);

            // Contar cuántos elementos hay por tipo en ese inventario
            for (TipoElemento tipo : tipos) {
                int cantidad = 0;
                for (Elemento elemento : elementosInventario) {
                    if (elemento.getTipo_elemento_id() == tipo.getId()) cantidad++;
                }
                tipo.setCantidadElementos(cantidad);
            }

            // Retornar resultados con éxito
            return ResponseProvider.success(tipos, "Tipos de elementos obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un tipo de elemento por su ID.
     *
     * @param id Identificador del tipo de elemento.
     * @return Tipo encontrado o error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTipoElemento(@PathParam("id") int id) {
        try {
            // Buscar el tipo por ID
            TipoElemento tipo = tipoDAO.getById(id);

            // Validar existencia
            if (tipo == null) {
                return ResponseProvider.error("Tipo de elemento no encontrado", 404);
            }

            // Retornar tipo encontrado
            return ResponseProvider.success(tipo, "Tipo de elemento obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo tipo de elemento en el sistema.
     * Se valida la unicidad del consecutivo antes de registrar.
     *
     * @param tipo Objeto TipoElemento recibido en el cuerpo de la petición.
     * @return Tipo creado o error si ya existe o falla.
     */
    @POST
    @ValidarCampos(entidad = "tipo_elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearTipoElemento(TipoElemento tipo) {
        try {
            // Verificar si ya existe un tipo con ese consecutivo
            TipoElemento existente = tipoDAO.getByConsecutivo(tipo.getConsecutivo());
            if (existente != null) {
                return ResponseProvider.error("Ya existe un tipo de elemento con este consecutivo", 409);
            }

            // Crear nuevo tipo
            TipoElemento nuevo = tipoDAO.create(tipo);
            if (nuevo != null) {
                return ResponseProvider.success(nuevo, "Tipo de elemento creado correctamente", 201);
            } else {
                return ResponseProvider.error("Error al crear el tipo de elemento", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un tipo de elemento existente.
     * Se valida existencia y unicidad del consecutivo.
     *
     * @param id ID del tipo a actualizar.
     * @param tipo Nuevos datos del tipo.
     * @return Tipo actualizado o error si no existe o hay duplicidad.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "tipo_elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarTipoElemento(@PathParam("id") int id, TipoElemento tipo) {
        try {
            // Verificar si el tipo a actualizar existe
            TipoElemento actual = tipoDAO.getById(id);
            if (actual == null) {
                return ResponseProvider.error("Tipo de elemento no encontrado", 404);
            }

            // Validar duplicidad de consecutivo
            TipoElemento otroConsecutivo = tipoDAO.getByConsecutivo(tipo.getConsecutivo());
            if (otroConsecutivo != null && otroConsecutivo.getId() != id) {
                return ResponseProvider.error("Ya existe un tipo de elemento con este consecutivo", 409);
            }

            // Realizar actualización
            TipoElemento actualizado = tipoDAO.update(id, tipo);
            if (actualizado != null) {
                return ResponseProvider.success(actualizado, "Tipo de elemento actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el tipo de elemento", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un tipo de elemento del sistema si no tiene elementos asociados.
     *
     * @param id ID del tipo a eliminar.
     * @return Mensaje de éxito o error si no existe o está asociado.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarTipoElemento(@PathParam("id") int id) {
        try {
            // Validar existencia del tipo
            TipoElemento tipo = tipoDAO.getById(id);
            if (tipo == null) {
                return ResponseProvider.error("Tipo de elemento no encontrado", 404);
            }

            // Validar si hay elementos relacionados
            List<Elemento> relacionados = elementoDAO.getAllByIdTipoElemento(id);
            if (relacionados != null && !relacionados.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el tipo de elemento porque tiene elementos asociados", 409);
            }

            // Eliminar tipo
            boolean eliminado = tipoDAO.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Tipo de elemento eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el tipo de elemento", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
