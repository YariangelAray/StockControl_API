package controller;

import middleware.ValidarCampos;
import model.entity.ProgramaFormacion;
import providers.ResponseProvider;
import service.ProgramaFormacionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con programas de formación.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar programas.
 *
 * Rutas disponibles:
 * - GET /programas: Listar todos los programas.
 * - GET /programas/{id}: Buscar programa por ID.
 * - POST /programas: Crear nuevo programa.
 * - PUT /programas/{id}: Actualizar programa existente.
 * - DELETE /programas/{id}: Eliminar programa.
 * 
 * @author Yariangel
 */
@Path("/programas-formacion") // Define la ruta base para este controlador
public class ProgramaFormacionController {

    ProgramaFormacionService service; // Instancia del servicio que maneja la lógica de negocio

    public ProgramaFormacionController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new ProgramaFormacionService();
    }

    /**
     * Obtiene todos los programas registrados en el sistema.
     *
     * @return Lista de programas o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los programas
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un programa por su ID único.
     *
     * @param id Identificador del programa.
     * @return Programa encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del programa
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrograma(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el programa por ID
            return service.obtenerPrograma(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo programa en el sistema.
     * Se valida el contenido con una clase Middleware (@ValidarCampos).
     *
     * @param programa Objeto ProgramaFormacion recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "programa_formacion") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearPrograma(ProgramaFormacion programa) {
        try {
            // Llama al servicio para crear un nuevo programa
            return service.crearPrograma(programa);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un programa existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del programa a actualizar.
     * @param programa Datos nuevos del programa.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del programa
    @ValidarCampos(entidad = "programa_formacion") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPrograma(@PathParam("id") int id, ProgramaFormacion programa) {
        try {
            // Llama al servicio para actualizar el programa
            return service.actualizarPrograma(id, programa);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un programa del sistema mediante su ID.
     *
     * @param id ID del programa a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del programa
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPrograma(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el programa
            return service.eliminarPrograma(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
