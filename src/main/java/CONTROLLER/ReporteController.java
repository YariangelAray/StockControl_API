package controller;

import middleware.ValidarCampos;
import service.ReporteService;
import model.entity.Reporte;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Controlador REST para gestionar operaciones relacionadas con los reportes.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar reportes.
 *
 * Rutas disponibles:
 * - GET /reportes: Listar todos los reportes.
 * - GET /reportes/{id}: Buscar reporte por ID.
 * - POST /reportes: Crear nuevo reporte.
 * - PUT /reportes/{id}: Actualizar reporte existente.
 * - DELETE /reportes/{id}: Eliminar reporte.
 * 
 * @author Yariangel
 */
@Path("/reportes") // Define la ruta base para este controlador
public class ReporteController {

    ReporteService service; // Instancia del servicio que maneja la lógica de negocio de reportes

    public ReporteController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new ReporteService();
    }

    /**
     * Obtiene todos los reportes registrados en el sistema.
     *
     * @return Lista de reportes o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los reportes
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un reporte por su ID único.
     *
     * @param id Identificador del reporte.
     * @return Reporte encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Path("/{id}") // Ruta que incluye el ID del reporte
    @Produces(MediaType.APPLICATION_JSON) // La respuesta será en formato JSON
    public Response obtenerReporte(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el reporte por ID
            return service.obtenerReporte(id);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en consola
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo reporte en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param reporte Objeto Reporte recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "reporte") // Activa la validación de campos para la entidad "reporte"
    @Consumes(MediaType.APPLICATION_JSON) // Indica que se recibe un JSON como entrada
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearReporte(Reporte reporte) {
        try {
            // Llama al servicio para crear un nuevo reporte
            return service.crearReporte(reporte);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un reporte existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del reporte a actualizar.
     * @param reporte Datos nuevos del reporte.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta con el ID del reporte a actualizar
    @ValidarCampos(entidad = "reporte") // Valida los campos antes de actualizar
    @Consumes(MediaType.APPLICATION_JSON) // Entrada en formato JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en formato JSON
    public Response actualizarReporte(@PathParam("id") int id, Reporte reporte) {
        try {
            // Llama al servicio para actualizar el reporte
            return service.actualizarReporte(id, reporte);
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un reporte del sistema mediante su ID.
     *
     * @param id ID del reporte a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta con el ID del reporte
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public Response eliminarReporte(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el reporte
            return service.eliminarReporte(id);
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
