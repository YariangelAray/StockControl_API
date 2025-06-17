package service;

import model.entity.Usuario;
import model.dao.UsuarioDAO;
import providers.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Servicio que maneja la lógica de negocio relacionada con usuarios.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerTodosPorIdRol(int idRol)
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
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new UsuarioDAO();
    }

    /**
     * Retorna todos los usuarios registrados en la base de datos.
     *
     * @return Lista de usuarios o error si no hay resultados.
     */
    public Response obtenerTodos() {
        List<Usuario> usuarios = dao.getAll();

        if (usuarios.isEmpty()) {
            return ResponseProvider.error("No se encontraron usuarios", 404);
        }

        return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
    }

    /**
     * Retorna los usuarios que pertenecen a un rol específico.
     *
     * @param idRol ID del rol a filtrar.
     * @return Lista de usuarios del rol o error si no hay resultados.
     */
    public Response obtenerTodosPorIdRol(int idRol) {
        List<Usuario> usuarios = dao.getAllByIdRol(idRol);

        if (usuarios.isEmpty()) {
            return ResponseProvider.error("No se encontraron usuarios", 404);
        }

        return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o error si no existe.
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
     * @return Usuario encontrado o error si no existe.
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
     * @return Usuario encontrado o error si no existe.
     */
    public Response obtenerPorDocumento(String documento) {
        Usuario usuario = dao.getByDocumento(documento);

        if (usuario == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo usuario si su correo y documento no están registrados.
     *
     * @param usuario Objeto con los datos del nuevo usuario.
     * @return Usuario creado o mensaje de error si hay duplicados o falla el registro.
     */
    public Response crearUsuario(Usuario usuario) {
        // Validar que no exista un usuario con el mismo correo
        Usuario usuarioExistenteCorreo = dao.getByCorreo(usuario.getCorreo());
        if (usuarioExistenteCorreo != null) {
            return ResponseProvider.error("Este correo ya fue registrado", 400);
        }

        // Validar que no exista un usuario con el mismo documento
        Usuario usuarioExistenteDoc = dao.getByDocumento(usuario.getDocumento());
        if (usuarioExistenteDoc != null) {
            return ResponseProvider.error("Este número de documento ya fue registrado", 400);
        }

        // Intentar crear el usuario
        Usuario nuevoUsuario = dao.create(usuario);
        if (nuevoUsuario != null) {
            return ResponseProvider.success(nuevoUsuario, "Usuario creado correctamente", 201);
        } else {
            return ResponseProvider.error("Error al crear el usuario", 400);
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Objeto con los nuevos datos.
     * @return Usuario actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarUsuario(int id, Usuario usuario) {
        // Validar que el usuario exista
        Usuario usuarioExistente = dao.getById(id);
        if (usuarioExistente == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Intentar actualizar el usuario
        Usuario usuarioActualizado = dao.update(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseProvider.success(usuarioActualizado, "Usuario actualizado correctamente", 200);
        } else {
            return ResponseProvider.error("Error al actualizar el usuario", 404);
        }
    }

    /**
     * Elimina un usuario existente por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarUsuario(int id) {
        // Verificar existencia
        Usuario usuarioExistente = dao.getById(id);
        if (usuarioExistente == null) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Intentar eliminar
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            return ResponseProvider.success(null, "Usuario eliminado correctamente", 200);
        } else {
            return ResponseProvider.error("Error al eliminar el usuario", 500);
        }
    }
}