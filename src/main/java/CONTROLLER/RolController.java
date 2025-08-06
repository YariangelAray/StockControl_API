package controller;

import utils.ResponseProvider;
import model.dao.RolDAO;
import model.dao.UsuarioDAO;
import model.Rol;
import model.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los roles del sistema.
 *
 * Rutas disponibles:
 * - GET /roles             → Listar todos los roles.
 * - GET /roles/{id}        → Consultar rol por ID.
 * - POST /roles            → Crear nuevo rol.
 * - PUT /roles/{id}        → Actualizar rol existente.
 * - DELETE /roles/{id}     → Eliminar rol (solo si no está asociado a ningún usuario). 
 *
 * @author Yariangel Aray
 */
@Path("/roles") // Ruta base para las peticiones relacionadas con roles
public class RolController {

    // DAO para acceder a la tabla de roles
    private final RolDAO rolDAO;

    // DAO para verificar si hay usuarios asociados a un rol antes de eliminarlo
    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor del controlador.
     * Instancia los DAOs necesarios.
     */
    public RolController() {
        rolDAO = new RolDAO();
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Obtiene todos los roles registrados en el sistema.
     *
     * @return Respuesta con lista de roles (200) o mensaje de error si no hay datos (404).
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Consultar todos los roles desde la base de datos
            List<Rol> roles = rolDAO.getAll();

            // Verificar si la lista está vacía
            if (roles.isEmpty()) {
                return ResponseProvider.error("No se encontraron roles", 404);
            }

            // Retornar los roles encontrados con código 200
            return ResponseProvider.success(roles, "Roles obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en consola
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un rol específico por su ID.
     *
     * @param id ID del rol a buscar.
     * @return Respuesta con el rol (200) o mensaje de error si no se encuentra (404).
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRol(@PathParam("id") int id) {
        try {
            // Buscar el rol en base de datos usando su ID
            Rol rol = rolDAO.getById(id);

            // Verificar si el rol fue encontrado
            if (rol == null) {
                return ResponseProvider.error("Rol no encontrado", 404);
            }

            // Retornar el rol encontrado
            return ResponseProvider.success(rol, "Rol obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo rol en el sistema.
     *
     * @param rol Objeto con los datos del rol a crear.
     * @return Respuesta con el rol creado (201) o mensaje de error (400 o 500).
     */
    @POST
    @ValidarCampos(entidad = "rol")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearRol(Rol rol) {
        try {
            // Intentar crear el rol en la base de datos
            Rol nuevoRol = rolDAO.create(rol);

            // Verificar si el rol fue creado correctamente
            if (nuevoRol != null) {
                return ResponseProvider.success(nuevoRol, "Rol creado correctamente", 201);
            } else {
                return ResponseProvider.error("Error al crear el rol", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza los datos de un rol existente.
     *
     * @param id  ID del rol a actualizar.
     * @param rol Objeto con los nuevos datos del rol.
     * @return Respuesta con el rol actualizado (200) o mensaje de error (404 o 500).
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "rol")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarRol(@PathParam("id") int id, Rol rol) {
        try {
            // Validar que el rol exista en la base de datos
            Rol rolExistente = rolDAO.getById(id);
            if (rolExistente == null) {
                return ResponseProvider.error("Rol no encontrado", 404);
            }

            // Intentar actualizar el rol
            Rol rolActualizado = rolDAO.update(id, rol);
            if (rolActualizado != null) {
                return ResponseProvider.success(rolActualizado, "Rol actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el rol", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un rol del sistema, solo si no tiene usuarios asociados.
     *
     * @param id ID del rol a eliminar.
     * @return Respuesta con éxito (200), conflicto (409) o error (404 o 500).
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarRol(@PathParam("id") int id) {
        try {
            // Verificar si el rol existe
            Rol rolExistente = rolDAO.getById(id);
            if (rolExistente == null) {
                return ResponseProvider.error("Rol no encontrado", 404);
            }

            // Obtener usuarios que usan ese rol
            List<Usuario> usuariosConRol = usuarioDAO.getAllByIdRol(id);

            // Verificar si hay usuarios asociados
            if (usuariosConRol != null && !usuariosConRol.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el rol porque tiene usuarios asociados", 409);
            }

            // Intentar eliminar el rol
            boolean eliminado = rolDAO.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Rol eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el rol", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
