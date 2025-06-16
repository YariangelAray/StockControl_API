package CONTROLLER;

import SERVICE.UsuarioService;
import MODEL.Usuario;
import PROVIDERS.ResponseProvider;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador para gestionar las rutas HTTP relacionadas con los usuarios.
 * Expone servicios REST para crear, consultar, actualizar y eliminar usuarios.
 * También permite buscar por correo o documento.
 * 
 * Rutas disponibles:
 * - GET /usuarios
 * - GET /usuarios/{id}
 * - GET /usuarios/correo/{correo}
 * - GET /usuarios/documento/{documento}
 * - POST /usuarios
 * - PUT /usuarios/{id}
 * - DELETE /usuarios/{id}
 * 
 * @author Yariangel Aray
 */
@Path("/usuarios")
public class UsuarioController {

    UsuarioService service;

    public UsuarioController() {
        service = new UsuarioService();
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de usuarios en formato JSON o error.
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
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario a consultar.
     * @return Usuario encontrado o mensaje de error.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("id") int id) {
        try {
            return service.obtenerUsuario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un usuario por su documento.
     *
     * @param documento Número de documento.
     * @return Usuario encontrado o mensaje de error.
     */
    @GET
    @Path("/documento/{documento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorDocumento(@PathParam("documento") String documento) {
        try {
            return service.obtenerPorDocumento(documento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    /**
     * Obtiene un usuario por su correo.
     *
     * @param correo Correo del usuario.
     * @return Usuario encontrado o mensaje de error.
     */
    @GET
    @Path("/correo/{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorCorreo(@PathParam("correo") String correo) {
        try {
            return service.obtenerPorCorreo(correo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario Usuario a registrar.
     * @return Mensaje de éxito o error.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(Usuario usuario) {
        try {
            return service.crearUsuario(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Nuevos datos del usuario.
     * @return Mensaje de éxito o error.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") int id, Usuario usuario) {
        try {
            return service.actualizarUsuario(id, usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un usuario por ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Mensaje de éxito o error.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(@PathParam("id") int id) {
        try {
            return service.eliminarUsuario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
