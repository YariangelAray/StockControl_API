package SERVICE;

import MODEL.Usuario;
import MODEL.UsuarioDAO;
import PROVIDERS.ResponseProvider;
import java.util.List;
import javax.ws.rs.core.*;

/**
 * Servicio para gestionar operaciones relacionadas con usuarios.
 * Actúa como intermediario entre el controlador (API) y la capa DAO (acceso a datos).
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerUsuario(int id)
 * - obtenerPorDocumento(String documento)
 * - obtenerPorCorreo(String correo)
 * - crearUsuario(Usuario usuario)
 * - actualizarUsuario(int id, Usuario usuario)
 * - eliminarUsuario(int id)
 * 
 * @author Yariangel Aray
 */
public class UsuarioService {
    
    private UsuarioDAO dao;

    public UsuarioService() {
        dao = new UsuarioDAO(); // Se crea el DAO para delegar las operaciones de base de datos
    }

    /**
     * Retorna la lista completa de usuarios.
     *
     * @return Lista de objetos Usuario.
     */
    public Response obtenerTodos() {
        List<Usuario> usuarios = dao.getAll();
        if (usuarios.isEmpty()) {
            return ResponseProvider.error("No se encontraron usuarios", 404);
        }
        return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
    }
    
    /**
     * Retorna la lista completa de usuarios que sean de un Rol determinado.
     * 
     * @param id ID del Rol.
     * @return Lista de objetos Usuario.
     */
    public Response obtenerTodosPorIdRol(int idRol) {
        List<Usuario> usuarios = dao.getAll();
        if (usuarios.isEmpty()) {
            return ResponseProvider.error("No se encontraron usuarios", 404);
        }
        return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
    }
    
    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o null.
     */
    public Response obtenerUsuario(int id) {
        Usuario usuario = dao.getById(id);
        if (usuario == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }
        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo a buscar.
     * @return Usuario encontrado o null.
     */
    public Response obtenerPorCorreo(String correo) {
        Usuario usuario = dao.getByCorreo(correo);
        if (usuario == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }
        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
    }

    /**
     * Busca un usuario por su número de documento.
     *
     * @param documento Documento a buscar.
     * @return Usuario encontrado o null.
     */
    public Response obtenerPorDocumento(String documento) {
        Usuario usuario = dao.getByDocumento(documento);
        if (usuario == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }
        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);        
    }
    
    /**
     * Crea un nuevo usuario.
     *
     * @param usuario Objeto Usuario a crear.
     * @return true si se creó exitosamente, false si falló.
     */
    public Response crearUsuario(Usuario usuario) {        
        Usuario usuarioExistenteCorreo = dao.getByCorreo(usuario.getCorreo());
        if (usuarioExistenteCorreo != null){
            return ResponseProvider.error("Este correo ya fue registrado", 400);
        }
        
        Usuario usuarioExistenteDoc = dao.getByDocumento(usuario.getDocumento());
        if (usuarioExistenteDoc != null){
            return ResponseProvider.error("Este número de documento ya fue registrado", 400);
        }
        
        Usuario nuevoUsuario = dao.create(usuario);      
        if (nuevoUsuario != null) {
            return ResponseProvider.success(nuevoUsuario, "Usuario creado correctamente", 201);
        } else {
            return ResponseProvider.error("Error al crear el usuario", 400);
        }
    }

    /**
     * Actualiza un usuario existente por ID.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Nuevos datos del usuario.
     * @return true si se actualizó correctamente, false si falló.
     */
    public Response actualizarUsuario(int id, Usuario usuario) {
        Usuario usuarioExistente = dao.getById(id);
        if (usuarioExistente == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }
        Usuario usuarioActualizado = dao.update(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseProvider.success(usuarioActualizado, "Usuario actualizado correctamente", 200);
        } else {
            return ResponseProvider.error("Error al actualizar el usuario", 404);
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si se eliminó, false si falló.
     */
    public Response eliminarUsuario(int id) {
        Usuario usuarioExistente = dao.getById(id);
        if (usuarioExistente == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            return ResponseProvider.success(null, "Usuario eliminado correctamente", 200);
        } else {
            return ResponseProvider.error("Error al eliminar el usuario", 500);
        }
    }
}
