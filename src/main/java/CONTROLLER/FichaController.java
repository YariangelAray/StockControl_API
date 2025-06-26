package controller;

// Importaciones necesarias para el funcionamiento del controlador
import middleware.ValidarCampos;
import model.entity.Ficha;
import service.FichaService;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con las fichas.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar fichas.
 *
 * Rutas disponibles:
 * - GET /fichas: Listar todas las fichas.
 * - GET /fichas/{id}: Buscar ficha por ID.
 * - POST /fichas: Crear nueva ficha.
 * - PUT /fichas/{id}: Actualizar ficha existente.
 * - DELETE /fichas/{id}: Eliminar ficha si no tiene usuarios asociados.
 * 
 * @author Yariangel Aray
 */
@Path("/fichas") // Ruta base para este controlador
public class FichaController {

    // Instancia del servicio que contiene la lógica de negocio de las fichas
    private FichaService service;

    // Constructor que instancia el servicio al crear el controlador
    public FichaController() {
        service = new FichaService();
    }

    /**
     * Obtiene todas las fichas registradas en el sistema.
     *
     * @return Lista de fichas o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // La respuesta será en formato JSON
    public Response obtenerTodas() {
        try {
            // Llama al servicio para obtener todas las fichas
            return service.obtenerTodas();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error en la consola para debug
            // Retorna error 500 si algo falla
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene una ficha específica por su ID.
     *
     * @param id ID de la ficha a buscar.
     * @return Ficha encontrada o mensaje de error si no existe.
     */
    @GET
    @Path("/{id}") // Ruta dinámica que incluye el ID de la ficha
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerFicha(@PathParam("id") int id) {
        try {
            // Llama al servicio para buscar una ficha por ID
            return service.obtenerFicha(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea una nueva ficha en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param ficha Objeto ficha recibido en la petición.
     * @return Respuesta con el resultado del registro.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "ficha") // Activación del middleware de validación
    @Consumes(MediaType.APPLICATION_JSON) // El cuerpo de la petición debe ser JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearFicha(Ficha ficha) {
        try {
            // Llama al servicio para crear la ficha
            return service.crearFicha(ficha);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza una ficha existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID de la ficha a actualizar.
     * @param ficha Datos nuevos de la ficha.
     * @return Resultado de la operación.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta con ID dinámico
    @ValidarCampos(entidad = "ficha") // Activación del middleware de validación
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarFicha(@PathParam("id") int id, Ficha ficha) {
        try {
            // Llama al servicio para actualizar la ficha
            return service.actualizarFicha(id, ficha);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina una ficha específica si no tiene usuarios asociados.
     *
     * @param id ID de la ficha a eliminar.
     * @return Respuesta indicando el resultado.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta dinámica con ID
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarFicha(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar la ficha
            return service.eliminarFicha(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
