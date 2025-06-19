package service;

import java.util.ArrayList;
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

    private UsuarioDAO dao; // Instancia del DAO para acceder a los datos de usuarios

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
        // Obtiene la lista de usuarios desde el DAO
        List<Usuario> usuarios = dao.getAll();

        // Verifica si la lista está vacía
        if (usuarios.isEmpty()) {
            // Retorna un error si no se encontraron usuarios
            return ResponseProvider.error("No se encontraron usuarios", 404);
        }

        // Retorna la lista de usuarios si se encontraron
        return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o error si no existe.
     */
    public Response obtenerUsuario(int id) {
        // Busca el usuario por ID en el DAO
        Usuario usuario = dao.getById(id);

        // Verifica si el usuario fue encontrado
        if (usuario == null) {
            // Retorna un error si no se encontró el usuario
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Retorna el usuario si fue encontrado
        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo a buscar.
     * @return Usuario encontrado o error si no existe.
     */
    public Response obtenerPorCorreo(String correo) {
        // Busca el usuario por correo en el DAO
        Usuario usuario = dao.getByCorreo(correo);

        // Verifica si el usuario fue encontrado
        if (usuario == null) {
            // Retorna un error si no se encontró el usuario
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Retorna el usuario si fue encontrado
        return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
    }

    /**
     * Busca un usuario por su número de documento.
     *
     * @param documento Documento a buscar.
     * @return Usuario encontrado o error si no existe.
     */
    public Response obtenerPorDocumento(String documento) {
        // Busca el usuario por documento en el DAO
        Usuario usuario = dao.getByDocumento(documento);

        // Verifica si el usuario fue encontrado
        if (usuario == null) {
            // Retorna un error si no se encontró el usuario
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Retorna el usuario si fue encontrado
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
            // Retorna un error si el correo ya está registrado
            return ResponseProvider.error("Este correo ya fue registrado", 400);
        }

        // Validar que no exista un usuario con el mismo documento
        Usuario usuarioExistenteDoc = dao.getByDocumento(usuario.getDocumento());
        if (usuarioExistenteDoc != null) {
            // Retorna un error si el documento ya está registrado
            return ResponseProvider.error("Este número de documento ya fue registrado", 400);
        }

        // Intentar crear el usuario en la base de datos
        Usuario nuevoUsuario = dao.create(usuario);
        if (nuevoUsuario != null) {
            // Retorna el nuevo usuario si fue creado correctamente
            return ResponseProvider.success(nuevoUsuario, "Usuario creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el usuario
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
            // Retorna un error si el usuario no fue encontrado
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        List<Usuario> usuarios = dao.getAll();
        List<Usuario> usuariosRegistrados = new ArrayList<>();
        
        for (Usuario usuarioRegistrado : usuarios)
            if (usuarioRegistrado.getId() != id) usuariosRegistrados.add(usuarioRegistrado);
        
        for (Usuario usuarioRegistrado : usuariosRegistrados)
            if (usuarioRegistrado.getDocumento()== usuario.getDocumento()){
                return ResponseProvider.error("Este número de documento ya fue registrado", 400);
            }
        
        for (Usuario usuarioRegistrado : usuariosRegistrados)
            if (usuarioRegistrado.getCorreo()== usuario.getCorreo()){
                return ResponseProvider.error("Este correo ya fue registrado", 400);
            }
        
        // Intentar actualizar el usuario en la base de datos
        Usuario usuarioActualizado = dao.update(id, usuario);
        if (usuarioActualizado != null) {
            // Retorna el usuario actualizado si fue exitoso
            return ResponseProvider.success(usuarioActualizado, "Usuario actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el usuario
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
        // Verificar existencia del usuario
        Usuario usuarioExistente = dao.getById(id);
        if (usuarioExistente == null) {
            // Retorna un error si el usuario no fue encontrado
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Intentar eliminar el usuario de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el usuario fue eliminado
            return ResponseProvider.success(null, "Usuario eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el usuario
            return ResponseProvider.error("Error al eliminar el usuario", 500);
        }
    }
}
