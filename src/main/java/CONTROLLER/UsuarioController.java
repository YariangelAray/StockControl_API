package CONTROLLER;

import SERVICE.UsuarioService;
import MODEL.Usuario;
import java.util.List;
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
            List<Usuario> usuarios = service.obtenerTodos();
            if (usuarios == null || usuarios.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontraron usuarios\"}")
                        .build();
            }
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al obtener los usuarios.\"}")
                    .build();
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
            Usuario usuario = service.obtenerUsuario(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Usuario no encontrado\"}")
                        .build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al obtener el usuario.\"}")
                    .build();
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
            Usuario usuario = service.obtenerPorDocumento(documento);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Usuario no encontrado con ese documento\"}")
                        .build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al buscar el usuario por documento.\"}")
                    .build();
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
            Usuario usuario = service.obtenerPorCorreo(correo);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Usuario no encontrado con ese correo\"}")
                        .build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al buscar el usuario por correo.\"}")
                    .build();
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
        boolean creado = service.crearUsuario(usuario);
        if (creado) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Usuario creado exitosamente\"}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error al crear el usuario\"}")
                    .build();
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
            boolean actualizado = service.actualizarUsuario(id, usuario);
            if (actualizado) {
                return Response.ok("{\"mensaje\": \"Usuario actualizado exitosamente\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Usuario no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al actualizar el usuario.\"}")
                    .build();
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
            boolean eliminado = service.eliminarUsuario(id);
            if (eliminado) {
                return Response.ok("{\"mensaje\": \"Usuario eliminado exitosamente\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Usuario no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al eliminar el usuario.\"}")
                    .build();
        }
    }
}
