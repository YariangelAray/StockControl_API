package controller;

import middleware.ValidarCampos;
import service.EstadoService;
import model.entity.Estado;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los estados.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar estados.
 *
 * Rutas disponibles:
 * - GET /estados: Listar todos los estados.
 * - GET /estados/{id}: Buscar estado por ID.
 * - POST /estados: Crear nuevo estado.
 * - PUT /estados/{id}: Actualizar estado existente.
 * - DELETE /estados/{id}: Eliminar estado.
 * 
 * Usa la validación con @ValidarCampos para asegurar que los datos sean válidos.
 * 
 * @autor Yariangel Aray
 */
@Path("/estados") // Define la ruta base para este controlador
public class EstadoController {

    EstadoService service; // Instancia del servicio que maneja la lógica de negocio

    public EstadoController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new EstadoService();
    }

    /**
     * Obtiene todos los estados registrados en el sistema.
     *
     * @return Lista de estados o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los estados
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un estado por su ID único.
     *
     * @param id Identificador del estado.
     * @return Estado encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del estado
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstado(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el estado por ID
            return service.obtenerEstado(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo estado en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param estado Objeto Estado recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "estado") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearEstado(Estado estado) {
        try {
            // Llama al servicio para crear un nuevo estado
            return service.crearEstado(estado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un estado existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del estado a actualizar.
     * @param estado Datos nuevos del estado.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del estado
    @ValidarCampos(entidad = "estado") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstado(@PathParam("id") int id, Estado estado) {
        try {
            // Llama al servicio para actualizar el estado
            return service.actualizarEstado(id, estado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un estado del sistema mediante su ID.
     *
     * @param id ID del estado a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del estado
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEstado(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el estado
            return service.eliminarEstado(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
