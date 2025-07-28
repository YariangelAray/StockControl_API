package controller;

import middleware.ValidarCampos;
import service.InventarioService;
import model.entity.Inventario;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los inventarios.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar inventarios.
 *
 * Rutas disponibles:
 * - GET /inventarios: Listar todos los inventarios.
 * - GET /inventarios/usuario/{id}: Buscar inventarios por id de usuario admin.
 * - GET /inventarios/{id}/ambientes: Busca todos los ambientes que tengan elementos de un inventario.
 * - GET /inventarios/{id}: Buscar inventario por ID.
 * - POST /inventarios: Crear nuevo inventario.
 * - PUT /inventarios/{id}: Actualizar inventario existente.
 * - DELETE /inventarios/{id}: Eliminar inventario.
 * 
 * @author Yariangel Aray
 */
@Path("/inventarios") // Define la ruta base para este controlador
public class InventarioController {

    InventarioService service; // Instancia del servicio que maneja la lógica de negocio

    public InventarioController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new InventarioService();
    }

    /**
     * Obtiene todos los inventarios registrados en el sistema.
     *
     * @return Lista de inventarios o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los inventarios
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca los inventarios de un administrador
     *
     * @param id Identificador del usuario admin.
     * @return Inventario encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/usuario/{id}") // Ruta que incluye el ID del usuario administrador
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInventariosUserAdmin(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el inventario por ID
            return service.obtenerTodosUsuarioAdmin(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    /**
    * Busca los ambientes que esten cubiertos por un inventario
    *
    * @param idInventario Objeto con los nuevos datos.
    * @return Ambientes cubiertos o mensaje de error si no hay o falla la busqueda.
    */
    @GET
    @Path("/{id}/ambientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAmbientesPorInventario(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el inventario por ID
            return service.obtenerAmbientesPorInventario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    /**
     * Busca un inventario por su ID único.
     *
     * @param id Identificador del inventario.
     * @return Inventario encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del inventario
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInventario(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el inventario por ID
            return service.obtenerInventario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo inventario en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param inventario Objeto Inventario recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "inventario") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearInventario(Inventario inventario) {
        try {
            // Llama al servicio para crear un nuevo inventario
            return service.crearInventario(inventario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un inventario existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del inventario a actualizar.
     * @param inventario Datos nuevos del inventario.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del inventario
    @ValidarCampos(entidad = "inventario") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarInventario(@PathParam("id") int id, Inventario inventario) {
        try {
            // Llama al servicio para actualizar el inventario
            return service.actualizarInventario(id, inventario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un inventario del sistema mediante su ID.
     *
     * @param id ID del inventario a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del inventario
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarInventario(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el inventario
            return service.eliminarInventario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
