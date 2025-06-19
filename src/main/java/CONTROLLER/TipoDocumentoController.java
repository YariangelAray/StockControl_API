package controller;

import middleware.ValidarCampos;
import service.TipoDocumentoService;
import model.entity.TipoDocumento;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los tipos de documento.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar tipos de documento.
 *
 * Rutas disponibles:
 * - GET /tipos-documento: Listar todos los tipos.
 * - GET /tipos-documento/{id}: Buscar tipo por ID.
 * - POST /tipos-documento: Crear nuevo tipo.
 * - PUT /tipos-documento/{id}: Actualizar tipo existente.
 * - DELETE /tipos-documento/{id}: Eliminar tipo.
 *
 * @author Yariangel Aray
 */
@Path("/tipos-documento") // Define la ruta base para este controlador
public class TipoDocumentoController {

    TipoDocumentoService service; // Instancia del servicio que maneja la lógica de negocio

    public TipoDocumentoController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new TipoDocumentoService();
    }

    /**
     * Obtiene todos los tipos registrados en el sistema.
     *
     * @return Lista de tipos o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los tipos
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un tipo por su ID único.
     *
     * @param id Identificador del tipo.
     * @return Tipo encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del tipo
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTipo(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el tipo por ID
            return service.obtenerTipo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo tipo en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param tipo Objeto TipoDocumento recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "tipo_documento") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearTipo(TipoDocumento tipo) {
        try {
            // Llama al servicio para crear un nuevo tipo
            return service.crearTipo(tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un tipo existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del tipo a actualizar.
     * @param tipo Datos nuevos del tipo.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del tipo
    @ValidarCampos(entidad = "tipo_documento") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarTipo(@PathParam("id") int id, TipoDocumento tipo) {
        try {
            // Llama al servicio para actualizar el tipo
            return service.actualizarTipo(id, tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un tipo del sistema mediante su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del tipo
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarTipo(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el tipo
            return service.eliminarTipo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
