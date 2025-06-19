package service;

import model.entity.Rol;
import model.dao.RolDAO;
import providers.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.UsuarioDAO;
import model.entity.Usuario;

/**
 * Servicio que maneja la lógica de negocio relacionada con roles.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerRol(int id)
 * - crearRol(Rol rol)
 * - actualizarRol(int id, Rol rol)
 * - eliminarRol(int id)
 * 
 * @author Yariangel Aray
 */
public class RolService {

    private RolDAO dao; // Instancia del DAO para acceder a los datos de roles

    public RolService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new RolDAO();
    }

    /**
     * Retorna todos los roles registrados en la base de datos.
     *
     * @return Lista de roles o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de roles desde el DAO
        List<Rol> roles = dao.getAll();

        // Verifica si la lista está vacía
        if (roles.isEmpty()) {
            // Retorna un error si no se encontraron roles
            return ResponseProvider.error("No se encontraron roles", 404);
        }

        // Retorna la lista de roles si se encontraron
        return ResponseProvider.success(roles, "Roles obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un rol por su ID.
     *
     * @param id ID del rol.
     * @return Rol encontrado o error si no existe.
     */
    public Response obtenerRol(int id) {
        // Busca el rol por ID en el DAO
        Rol rol = dao.getById(id);

        // Verifica si el rol fue encontrado
        if (rol == null) {
            // Retorna un error si no se encontró el rol
            return ResponseProvider.error("Rol no encontrado", 404);
        }

        // Retorna el rol si fue encontrado
        return ResponseProvider.success(rol, "Rol obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo rol.
     *
     * @param rol Objeto con los datos del nuevo rol.
     * @return Rol creado o mensaje de error si falla el registro.
     */
    public Response crearRol(Rol rol) {
        // Intentar crear el rol en la base de datos
        Rol nuevoRol = dao.create(rol);
        if (nuevoRol != null) {
            // Retorna el nuevo rol si fue creado correctamente
            return ResponseProvider.success(nuevoRol, "Rol creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el rol
            return ResponseProvider.error("Error al crear el rol", 400);
        }
    }

    /**
     * Actualiza los datos de un rol existente.
     *
     * @param id ID del rol a actualizar.
     * @param rol Objeto con los nuevos datos.
     * @return Rol actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarRol(int id, Rol rol) {
        // Validar que el rol exista
        Rol rolExistente = dao.getById(id);
        if (rolExistente == null) {
            // Retorna un error si el rol no fue encontrado
            return ResponseProvider.error("Rol no encontrado", 404);
        }

        // Intentar actualizar el rol en la base de datos
        Rol rolActualizado = dao.update(id, rol);
        if (rolActualizado != null) {
            // Retorna el rol actualizado si fue exitoso
            return ResponseProvider.success(rolActualizado, "Rol actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el rol
            return ResponseProvider.error("Error al actualizar el rol", 404);
        }
    }

    /**
     * Elimina un rol existente por su ID.
     *
     * @param id ID del rol a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarRol(int id) {
        // Verificar existencia del rol
        Rol rolExistente = dao.getById(id);
        if (rolExistente == null) {
            // Retorna un error si el rol no fue encontrado
            return ResponseProvider.error("Rol no encontrado", 404);
        }
        
        // Se crea una instancia de UsuarioDAO para acceder a los usuarios
        UsuarioDAO usuarioDao = new UsuarioDAO();

        // Se obtiene la lista de usuarios que tienen asignado el rol con el ID proporcionado
        List<Usuario> usuarios = usuarioDao.getAllByIdRol(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay usuarios asociados a ese rol)
        if (usuarios != null && !usuarios.isEmpty())
            // Si hay usuarios asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar el rol
            return ResponseProvider.error("No se puede eliminar el rol porque tiene usuarios asociados", 409);

        // Intentar eliminar el rol de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el rol fue eliminado
            return ResponseProvider.success(null, "Rol eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el rol
            return ResponseProvider.error("Error al eliminar el rol", 500);
        }
    }
}
