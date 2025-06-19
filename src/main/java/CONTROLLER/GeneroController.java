package controller;

import middleware.ValidarCampos;
import service.GeneroService;
import model.entity.Genero;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los géneros.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar géneros.
 *
 * Rutas disponibles:
 * - GET /generos: Listar todos los géneros.
 * - GET /generos/{id}: Buscar género por ID.
 * - POST /generos: Crear nuevo género.
 * - PUT /generos/{id}: Actualizar género existente.
 * - DELETE /generos/{id}: Eliminar género.
 * 
 * @author Yariangel Aray
 */
@Path("/generos") // Define la ruta base para este controlador
public class GeneroController {

    GeneroService service; // Instancia del servicio que maneja la lógica de negocio

    public GeneroController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new GeneroService();
    }

    /**
     * Obtiene todos los géneros registrados en el sistema.
     *
     * @return Lista de géneros o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los géneros
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un género por su ID único.
     *
     * @param id Identificador del género.
     * @return Género encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del género
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerGenero(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el género por ID
            return service.obtenerGenero(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo género en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param genero Objeto Genero recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "genero") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearGenero(Genero genero) {
        try {
            // Llama al servicio para crear un nuevo género
            return service.crearGenero(genero);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un género existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del género a actualizar.
     * @param genero Datos nuevos del género.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del género
    @ValidarCampos(entidad = "genero") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarGenero(@PathParam("id") int id, Genero genero) {
        try {
            // Llama al servicio para actualizar el género
            return service.actualizarGenero(id, genero);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un género del sistema mediante su ID.
     *
     * @param id ID del género a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del género
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarGenero(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el género
            return service.eliminarGenero(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
