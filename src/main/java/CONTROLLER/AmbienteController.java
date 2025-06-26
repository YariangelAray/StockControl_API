package controller;

import middleware.ValidarCampos;
import service.AmbienteService;
import model.entity.Ambiente;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los ambientes.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar ambientes.
 *
 * Rutas disponibles:
 * - GET /ambientes: Listar todos los ambientes.
 * - GET /ambientes/{id}: Buscar ambiente por ID.
 * - POST /ambientes: Crear nuevo ambiente.
 * - PUT /ambientes/{id}: Actualizar ambiente existente.
 * - DELETE /ambientes/{id}: Eliminar ambiente.
 * 
 * @author Yariangel Aray
 */
@Path("/ambientes") // Define la ruta base para este controlador
public class AmbienteController {

    AmbienteService service; // Instancia del servicio que maneja la lógica de negocio

    public AmbienteController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new AmbienteService();
    }

    /**
     * Obtiene todos los ambientes registrados en el sistema.
     *
     * @return Lista de ambientes o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los ambientes
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un ambiente por su ID único.
     *
     * @param id Identificador del ambiente.
     * @return Ambiente encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del ambiente
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAmbiente(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el ambiente por ID
            return service.obtenerAmbiente(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo ambiente en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param ambiente Objeto Ambiente recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "ambiente") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearAmbiente(Ambiente ambiente) {
        try {
            // Llama al servicio para crear un nuevo ambiente
            return service.crearAmbiente(ambiente);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un ambiente existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del ambiente a actualizar.
     * @param ambiente Datos nuevos del ambiente.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del ambiente
    @ValidarCampos(entidad = "ambiente") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarAmbiente(@PathParam("id") int id, Ambiente ambiente) {
        try {
            // Llama al servicio para actualizar el ambiente
            return service.actualizarAmbiente(id, ambiente);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un ambiente del sistema mediante su ID.
     *
     * @param id ID del ambiente a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del ambiente
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarAmbiente(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el ambiente
            return service.eliminarAmbiente(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
