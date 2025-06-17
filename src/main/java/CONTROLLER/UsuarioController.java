package controller;

import middleware.ValidarCampos;
import service.UsuarioService;
import model.entity.Usuario;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar usuarios.
 *
 * Rutas disponibles:
 * - GET /usuarios: Listar todos los usuarios.
 * - GET /usuarios/{id}: Buscar usuario por ID.
 * - GET /usuarios/correo/{correo}: Buscar usuario por correo.
 * - GET /usuarios/documento/{documento}: Buscar usuario por documento.
 * - POST /usuarios: Crear nuevo usuario.
 * - PUT /usuarios/{id}: Actualizar usuario existente.
 * - DELETE /usuarios/{id}: Eliminar usuario.
 *
 * @author Yariangel Aray
 */
@Path("/usuarios")
public class UsuarioController {

    UsuarioService service;

    public UsuarioController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new UsuarioService();
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Lista de usuarios o mensaje de error si ocurre una excepción.
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
     * Busca un usuario por su ID único.
     *
     * @param id Identificador del usuario.
     * @return Usuario encontrado o mensaje de error si no existe o ocurre una excepción.
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
     * Busca un usuario por su número de documento.
     *
     * @param documento Documento del usuario.
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
     * Busca un usuario por su correo electrónico.
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
     * Registra un nuevo usuario en el sistema.
     * Se valida el contenido con una clase Middleware (`@ValidarUsuarioCampos`).
     *
     * @param usuario Objeto Usuario recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST
    @ValidarCampos(entidad = "usuario")
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
     * Actualiza la información de un usuario existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Datos nuevos del usuario.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "usuario")
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
     * Elimina un usuario del sistema mediante su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
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
