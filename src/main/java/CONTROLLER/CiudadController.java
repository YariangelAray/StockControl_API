package controller;

import middleware.ValidarCampos;
import service.CiudadService;
import model.entity.Ciudad;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con las ciudades.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar ciudades.
 *
 * Rutas disponibles:
 * - GET /ciudades: Listar todas las ciudades.
 * - GET /ciudades/{id}: Buscar ciudad por ID.
 * - POST /ciudades: Crear nueva ciudad.
 * - PUT /ciudades/{id}: Actualizar ciudad existente.
 * - DELETE /ciudades/{id}: Eliminar ciudad.
 * 
 * @author Yariangel Aray
 */
@Path("/ciudades") // Define la ruta base para este controlador
public class CiudadController {

    CiudadService service; // Instancia del servicio que maneja la lógica de negocio

    public CiudadController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new CiudadService();
    }

    /**
     * Obtiene todas las ciudades registradas en el sistema.
     *
     * @return Lista de ciudades o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodas() {
        try {
            // Llama al servicio para obtener todas las ciudades
            return service.obtenerTodas();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca una ciudad por su ID único.
     *
     * @param id Identificador de la ciudad.
     * @return Ciudad encontrada o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID de la ciudad
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCiudad(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener la ciudad por ID
            return service.obtenerCiudad(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra una nueva ciudad en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param ciudad Objeto Ciudad recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "ciudad") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearCiudad(Ciudad ciudad) {
        try {
            // Llama al servicio para crear una nueva ciudad
            return service.crearCiudad(ciudad);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de una ciudad existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID de la ciudad a actualizar.
     * @param ciudad Datos nuevos de la ciudad.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID de la ciudad
    @ValidarCampos(entidad = "ciudad") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCiudad(@PathParam("id") int id, Ciudad ciudad) {
        try {
            // Llama al servicio para actualizar la ciudad
            return service.actualizarCiudad(id, ciudad);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina una ciudad del sistema mediante su ID.
     *
     * @param id ID de la ciudad a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID de la ciudad
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCiudad(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar la ciudad
            return service.eliminarCiudad(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
