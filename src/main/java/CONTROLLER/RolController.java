package controller;

import middleware.ValidarCampos;
import service.RolService;
import model.entity.Rol;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los roles.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar roles.
 *
 * Rutas disponibles:
 * - GET /roles: Listar todos los roles.
 * - GET /roles/{id}: Buscar rol por ID.
 * - POST /roles: Crear nuevo rol.
 * - PUT /roles/{id}: Actualizar rol existente.
 * - DELETE /roles/{id}: Eliminar rol.
 *
 * @author Yariangel Aray
 */
@Path("/roles") // Define la ruta base para este controlador
public class RolController {

    RolService service; // Instancia del servicio que maneja la lógica de negocio

    public RolController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new RolService();
    }

    /**
     * Obtiene todos los roles registrados en el sistema.
     *
     * @return Lista de roles o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los roles
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un rol por su ID único.
     *
     * @param id Identificador del rol.
     * @return Rol encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del rol
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRol(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el rol por ID
            return service.obtenerRol(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo rol en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param rol Objeto Rol recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "rol") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearRol(Rol rol) {
        try {
            // Llama al servicio para crear un nuevo rol
            return service.crearRol(rol);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un rol existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del rol a actualizar.
     * @param rol Datos nuevos del rol.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del rol
    @ValidarCampos(entidad = "rol") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarRol(@PathParam("id") int id, Rol rol) {
        try {
            // Llama al servicio para actualizar el rol
            return service.actualizarRol(id, rol);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un rol del sistema mediante su ID.
     *
     * @param id ID del rol a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del rol
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarRol(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el rol
            return service.eliminarRol(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
