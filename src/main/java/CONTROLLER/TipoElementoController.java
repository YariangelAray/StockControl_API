package controller;

import middleware.ValidarCampos;
import service.TipoElementoService;
import model.entity.TipoElemento;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los tipos de elementos.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar tipos de elementos.
 *
 * Rutas disponibles:
 * - GET /tipos-elementos: Listar todos los tipos de elementos.
 * - GET /tipos-elementos/{id}: Buscar tipo de elemento por ID.
 * - POST /tipos-elementos: Crear nuevo tipo de elemento.
 * - PUT /tipos-elementos/{id}: Actualizar tipo de elemento existente.
 * - DELETE /tipos-elementos/{id}: Eliminar tipo de elemento.
 * 
 * Este controlador actúa como puente entre la interfaz externa (cliente) y la lógica de negocio (servicio).
 * 
 * @author Yari
 */
@Path("/tipos-elementos") // Ruta base para este recurso
public class TipoElementoController {

    TipoElementoService service; // Instancia del servicio que contiene la lógica

    public TipoElementoController() {
        // Instancia el servicio
        service = new TipoElementoService();
    }

    /**
     * Obtiene todos los tipos de elementos registrados en el sistema.
     *
     * @return Lista de tipos o mensaje de error si ocurre una excepción.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    /**
     * Obtiene todos los tipos de elementos registrados en el sistema.
     * Pero le concatena la cantidad de elementos que tiene en ese inventario
     *
     * @return Lista de tipos o mensaje de error si ocurre una excepción.
     */
    @GET
    @Path("/inventario/{idInventario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosPorInventario(@PathParam("idInventario") int id) {
        try {
            return service.obtenerTodosPorInventario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un tipo de elemento por su ID.
     *
     * @param id Identificador del tipo de elemento.
     * @return Tipo de elemento encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTipoElemento(@PathParam("id") int id) {
        try {
            return service.obtenerTipoElemento(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo tipo de elemento en el sistema.
     * Se validan los campos mediante @ValidarCampos.
     *
     * @param tipo Objeto TipoElemento recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST
    @ValidarCampos(entidad = "tipo_elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearTipoElemento(TipoElemento tipo) {
        try {
            return service.crearTipoElemento(tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un tipo de elemento existente.
     * Se validan los campos antes de aplicar los cambios.
     *
     * @param id ID del tipo de elemento a actualizar.
     * @param tipo Nuevos datos del tipo.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "tipo_elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarTipoElemento(@PathParam("id") int id, TipoElemento tipo) {
        try {
            return service.actualizarTipoElemento(id, tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un tipo de elemento del sistema mediante su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarTipoElemento(@PathParam("id") int id) {
        try {
            return service.eliminarTipoElemento(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
