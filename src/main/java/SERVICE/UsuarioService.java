package SERVICE;

import MODEL.Usuario;
import MODEL.UsuarioDAO;
import java.util.List;

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
    public List<Usuario> obtenerTodos() {
        return dao.getAll();
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o null.
     */
    public Usuario obtenerUsuario(int id) {
        return dao.getById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo a buscar.
     * @return Usuario encontrado o null.
     */
    public Usuario obtenerPorCorreo(String correo) {
        return dao.getByCorreo(correo);
    }

    /**
     * Busca un usuario por su número de documento.
     *
     * @param documento Documento a buscar.
     * @return Usuario encontrado o null.
     */
    public Usuario obtenerPorDocumento(String documento) {
        return dao.getByDocumento(documento);
    }
    
    /**
     * Crea un nuevo usuario.
     *
     * @param usuario Objeto Usuario a crear.
     * @return true si se creó exitosamente, false si falló.
     */
    public boolean crearUsuario(Usuario usuario) {
        return dao.create(usuario);
    }

    /**
     * Actualiza un usuario existente por ID.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Nuevos datos del usuario.
     * @return true si se actualizó correctamente, false si falló.
     */
    public boolean actualizarUsuario(int id, Usuario usuario) {
        return dao.update(id, usuario);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si se eliminó, false si falló.
     */
    public boolean eliminarUsuario(int id) {
        return dao.delete(id);
    }
}
