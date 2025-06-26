package controller;

import middleware.ValidarCampos;
import service.ElementoService;
import model.entity.Elemento;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los elementos.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar elementos.
 *
 * Rutas disponibles:
 * - GET /elementos: Listar todos los elementos.
 * - GET /elementos/{id}: Buscar elemento por ID.
 * - POST /elementos: Crear nuevo elemento.
 * - PUT /elementos/{id}: Actualizar elemento existente.
 * - DELETE /elementos/{id}: Eliminar elemento.
 *
 * Al eliminar un elemento, se valida que no tenga reportes asociados.
 *
 * Autor: Yariangel Aray
 */
@Path("/elementos") // Ruta base para el controlador de elementos
public class ElementoController {

    ElementoService service; // Instancia del servicio que maneja la lógica de negocio

    public ElementoController() {
        service = new ElementoService(); // Inicializa el servicio
    }

    /**
     * Obtiene todos los elementos registrados en el sistema.
     *
     * @return Lista de elementos o mensaje de error si ocurre una excepción.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            return service.obtenerTodos(); // Solicita la lista al servicio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un elemento por su ID.
     *
     * @param id ID del elemento.
     * @return Elemento encontrado o error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerElemento(@PathParam("id") int id) {
        try {
            return service.obtenerElemento(id); // Solicita el elemento al servicio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo elemento en el sistema.
     *
     * @param elemento Objeto Elemento recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST
    @ValidarCampos(entidad = "elemento") // Middleware para validar los campos
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearElemento(Elemento elemento) {
        try {
            return service.crearElemento(elemento); // Solicita la creación al servicio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un elemento existente en el sistema.
     *
     * @param id ID del elemento.
     * @param elemento Nuevos datos del elemento.
     * @return Respuesta con estado y mensaje.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "elemento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarElemento(@PathParam("id") int id, Elemento elemento) {
        try {
            return service.actualizarElemento(id, elemento); // Solicita la actualización al servicio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un elemento del sistema si no tiene reportes asociados.
     *
     * @param id ID del elemento.
     * @return Respuesta con estado y mensaje.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarElemento(@PathParam("id") int id) {
        try {
            return service.eliminarElemento(id); // Solicita la eliminación al servicio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
