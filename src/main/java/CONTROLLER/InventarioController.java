package controller;

import utils.ResponseProvider;
import model.dao.ElementoDAO;
import model.dao.InventarioDAO;
import model.dto.AmbienteDTO;
import model.Elemento;
import model.Inventario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import model.CodigoAcceso;
import model.Usuario;
import model.dao.CodigoAccesoDAO;
import model.dao.UsuarioDAO;

/**
 * Controlador REST para gestionar operaciones relacionadas con los inventarios.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar inventarios.
 *
 * Rutas disponibles:
 * - GET /inventarios
 * - GET /inventarios/usuario/{id}
 * - GET /inventarios/{id}/ambientes
 * - GET /inventarios/{id}
 * - POST /inventarios
 * - PUT /inventarios/{id}
 * - DELETE /inventarios/{id}
 *
 * @author Yariangel Aray
 */
@Path("/inventarios")
public class InventarioController {

    // DAO principal para las operaciones de inventarios
    private InventarioDAO dao;

    // DAO único de elementos, se reutiliza en varios métodos
    private ElementoDAO elementoDAO;
    
    private UsuarioDAO usuarioDAO;

    // Constructor
    public InventarioController() {
        dao = new InventarioDAO(); // Instancia del DAO de inventario
        elementoDAO = new ElementoDAO(); // Instancia única del DAO de elementos
        usuarioDAO = new UsuarioDAO(); // Instancia adicional para validar usuario
    }

    /**
     * Complementa un objeto inventario con sus elementos, valor monetario total, cantidad de elementos
     * y número de ambientes cubiertos.
     *
     * @param inventario Objeto inventario a complementar con datos calculados.
     */
    private void complementarInventario(Inventario inventario) {
        List<Elemento> elementos = elementoDAO.getAllByIdInventario(inventario.getId()); // Obtener elementos
        inventario.setElementos(elementos); // Asignar elementos al inventario

        double totalValor = 0; // Inicializar suma
        for (Elemento elemento : elementos)
            totalValor += elemento.getValor_monetario(); // Sumar valores monetarios

        inventario.setValor_monetario(totalValor); // Establecer valor total
        inventario.setCantidad_elementos(elementos.size()); // Cantidad de elementos
        inventario.setAmbientes_cubiertos(dao.getAllAmbientesByInventario(inventario.getId()).size()); // Ambientes cubiertos
    }

    /**
     * Obtiene todos los inventarios registrados en el sistema con sus datos calculados.
     *
     * @return Respuesta HTTP con lista de inventarios y código 200 si fue exitoso,
     *         o error 404 si no se encontraron datos, o 500 si ocurrió un error interno.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            List<Inventario> inventarios = dao.getAll(); // Obtener lista de inventarios
            if (inventarios.isEmpty()) return ResponseProvider.error("No se encontraron inventarios", 404);

            // Complementar cada inventario
            for (Inventario inventario : inventarios) {
                complementarInventario(inventario);
            }

            return ResponseProvider.success(inventarios, "Inventarios obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene todos los inventarios registrados por un usuario administrador específico.
     *
     * @param id ID del usuario administrador.
     * @return Respuesta con lista de inventarios y código 200 si fue exitoso,
     *         204 si no hay inventarios, o 500 en caso de error.
     */
    @GET
    @Path("/usuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInventariosUserAdmin(@PathParam("id") int id) {
        try {
            List<Inventario> inventarios = dao.getAllByIdUserAdmin(id); // Buscar por ID de usuario
            if (inventarios.isEmpty()) return ResponseProvider.error("No se encontraron inventarios", 204);

            for (Inventario inventario : inventarios) {
                complementarInventario(inventario);
            }

            return ResponseProvider.success(inventarios, "Inventarios obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene los ambientes cubiertos por un inventario específico.
     *
     * @param idInventario ID del inventario del cual se desean consultar los ambientes.
     * @return Respuesta HTTP con lista de ambientes (DTO) y código 200 si fue exitoso,
     *         204 si no hay ambientes registrados, o 500 si ocurre un error.
     */
    @GET
    @Path("/{id}/ambientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAmbientesPorInventario(@PathParam("id") int idInventario) {
        try {
            List<AmbienteDTO> ambientes = dao.getAllAmbientesByInventario(idInventario); // Buscar ambientes
            if (ambientes.isEmpty()) return ResponseProvider.error("No se encontraron ambientes", 204);
            return ResponseProvider.success(ambientes, "Ambientes obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un inventario por su ID incluyendo sus elementos, valor monetario y cantidad.
     *
     * @param id ID del inventario a consultar.
     * @return Respuesta con el inventario y código 200 si fue exitoso,
     *         o error 404 si no se encuentra, o 500 si ocurre un error.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInventario(@PathParam("id") int id) {
        try {
            Inventario inventario = dao.getById(id); // Buscar inventario
            if (inventario == null) return ResponseProvider.error("Inventario no encontrado", 404);

            complementarInventario(inventario); // Completar sus datos
            return ResponseProvider.success(inventario, "Inventario obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo inventario en el sistema.
     *
     * @param inventario Objeto JSON que contiene los datos del inventario.
     * @return Respuesta con el inventario creado y código 201 si fue exitoso,
     *         o error 400 si ocurrió un fallo, o 500 en caso de excepción.
     */
    @POST
    @ValidarCampos(entidad = "inventario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearInventario(Inventario inventario) {
        try {
            // Validar que el usuario especificado como administrador exista
            Usuario usuario = usuarioDAO.getById(inventario.getUsuario_admin_id());
            if (usuario == null) {
                return ResponseProvider.error("El usuario administrativo especificado no existe.", 404);
            }

            // Validar que el usuario tenga el rol 2 (administrativo)
            if (usuario.getRol_id() != 2) {
                return ResponseProvider.error("Solo los usuarios con rol administrativo pueden tener inventarios.", 403);
            }
            
            Inventario nuevo = dao.create(inventario); // Crear nuevo
            Inventario creado = dao.getById(nuevo.getId()); // Obtener por ID

            if (nuevo != null && creado != null)
                return ResponseProvider.success(creado, "Inventario creado correctamente", 201);

            return ResponseProvider.error("Error al crear el inventario", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un inventario existente por su ID.
     *
     * @param id ID del inventario que se desea actualizar.
     * @param inventario Objeto JSON con los nuevos datos del inventario.
     * @return Respuesta con el inventario actualizado y código 200 si fue exitoso,
     *         o error 404 si no existe o no se pudo actualizar, o 500 si ocurre un error.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "inventario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarInventario(@PathParam("id") int id, Inventario inventario) {
        try {                        
            Inventario existente = dao.getById(id); // Verificar existencia
            if (existente == null)
                return ResponseProvider.error("Inventario no encontrado", 404);
            
            // Validar que el usuario especificado como administrador exista
            Usuario usuario = usuarioDAO.getById(inventario.getUsuario_admin_id());
            if (usuario == null) {
                return ResponseProvider.error("El usuario administrativo especificado no existe.", 404);
            }

            // Validar que el usuario tenga el rol 2 (administrativo)
            if (usuario.getRol_id() != 2) {
                return ResponseProvider.error("Solo los usuarios con rol administrativo pueden tener inventarios.", 403);
            }
            
            Inventario actualizado = dao.update(id, inventario); // Actualizar
            if (actualizado != null)
                return ResponseProvider.success(actualizado, "Inventario actualizado correctamente", 200);

            return ResponseProvider.error("Error al actualizar el inventario", 404);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un inventario si no tiene elementos asociados.
     *
     * @param id ID del inventario que se desea eliminar.
     * @return Respuesta con código 200 si se eliminó correctamente,
     *         409 si hay elementos asociados, 404 si no existe, o 500 en caso de error.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarInventario(@PathParam("id") int id) {
        try {
            Inventario existente = dao.getById(id); // Verificar existencia
            if (existente == null)
                return ResponseProvider.error("Inventario no encontrado", 404);

            List<Elemento> elementos = elementoDAO.getAllByIdInventario(id); // Verificar si tiene elementos
            if (elementos != null && !elementos.isEmpty())
                return ResponseProvider.error("No se puede eliminar el inventario porque tiene elementos asociados", 409);
            
            // Verificar que no tenga un código de acceso activo
            CodigoAccesoDAO codigoDAO = new CodigoAccesoDAO();
            CodigoAcceso codigoActivo = codigoDAO.getCodigoActivo(id);
            if (codigoActivo != null)
                return ResponseProvider.error("No se puede eliminar el inventario porque tiene un código de acceso activo", 409);

            boolean eliminado = dao.delete(id); // Intentar eliminar
            if (eliminado)
                return ResponseProvider.success(null, "Inventario eliminado correctamente", 200);

            return ResponseProvider.error("Error al eliminar el inventario", 500);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
