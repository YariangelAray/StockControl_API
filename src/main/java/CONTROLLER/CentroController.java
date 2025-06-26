package controller;

import middleware.ValidarCampos;
import service.CentroService;
import model.entity.Centro;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los centros.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar centros.
 *
 * Rutas disponibles:
 * - GET /centros: Listar todos los centros.
 * - GET /centros/{id}: Buscar centro por ID.
 * - POST /centros: Crear nuevo centro.
 * - PUT /centros/{id}: Actualizar centro existente.
 * - DELETE /centros/{id}: Eliminar centro.
 * 
 * @author Yariangel Aray
 */
@Path("/centros") // Define la ruta base para este controlador
public class CentroController {

    CentroService service; // Instancia del servicio que maneja la lógica de negocio

    public CentroController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new CentroService();
    }

    /**
     * Obtiene todos los centros registrados en el sistema.
     *
     * @return Lista de centros o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los centros
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un centro por su ID único.
     *
     * @param id Identificador del centro.
     * @return Centro encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del centro
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCentro(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el centro por ID
            return service.obtenerCentro(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo centro en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param centro Objeto Centro recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "centro") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearCentro(Centro centro) {
        try {
            // Llama al servicio para crear un nuevo centro
            return service.crearCentro(centro);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un centro existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del centro a actualizar.
     * @param centro Datos nuevos del centro.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del centro
    @ValidarCampos(entidad = "centro") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCentro(@PathParam("id") int id, Centro centro) {
        try {
            // Llama al servicio para actualizar el centro
            return service.actualizarCentro(id, centro);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un centro del sistema mediante su ID.
     *
     * @param id ID del centro a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del centro
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCentro(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el centro
            return service.eliminarCentro(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
