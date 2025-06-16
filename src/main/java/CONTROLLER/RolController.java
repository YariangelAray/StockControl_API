package CONTROLLER;

import MODEL.Rol;
import SERVICE.RolService;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador para gestionar las rutas HTTP relacionadas con los roles.
 * Expone servicios REST para crear, consultar, actualizar y eliminar roles. 
 * 
 * Rutas disponibles:
 * - GET /roles
 * - GET /roles/{id}
 * - POST /roles
 * - PUT /roles/{id}
 * - DELETE /roles/{id}
 * 
 * @author Yariangel Aray
 */
@Path("/roles")
public class RolController {
    
    RolService service;

    public RolController() {
        service = new RolService();
    }

    /**
     * Obtiene todos los roles registrados.
     *
     * @return Lista de roles en formato JSON o error.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {          
            List<Rol> roles = service.obtenerTodos();
            if (roles == null || roles.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontraron roles\"}")
                        .build();
            }
            return Response.ok(roles).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al obtener los roles.\"}")
                    .build();
        }
    }

    /**
     * Obtiene un rol por su ID.
     *
     * @param id ID del rol a consultar.
     * @return Rol encontrado o mensaje de error.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRol(@PathParam("id") int id) {
        try {
            Rol rol = service.obtenerRol(id);
            if (rol == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Rol no encontrado\"}")
                        .build();
            }
            return Response.ok(rol).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al obtener el rol.\"}")
                    .build();
        }
    }

    /**
     * Crea un nuevo rol.
     *
     * @param rol Rol a registrar.
     * @return Mensaje de éxito o error.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearRol(Rol rol) {
        boolean creado = service.crearRol(rol);
        if (creado) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Rol creado exitosamente\"}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error al crear el rol\"}")
                    .build();
        }
    }

    /**
     * Actualiza los datos de un rol existente.
     *
     * @param id ID del rol a actualizar.
     * @param rol Nuevos datos del rol.
     * @return Mensaje de éxito o error.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarRol(@PathParam("id") int id, Rol rol) {
        try {
            boolean actualizado = service.actualizarRol(id, rol);
            if (actualizado) {
                return Response.ok("{\"mensaje\": \"Rol actualizado exitosamente\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Rol no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al actualizar el rol.\"}")
                    .build();
        }
    }

    /**
     * Elimina un rol por ID.
     *
     * @param id ID del rol a eliminar.
     * @return Mensaje de éxito o error.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarRol(@PathParam("id") int id) {
        try {
            boolean eliminado = service.eliminarRol(id);
            if (eliminado) {
                return Response.ok("{\"mensaje\": \"Rol eliminado exitosamente\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"Rol no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Ocurrió un error al eliminar el rol.\"}")
                    .build();
        }
    }
}
