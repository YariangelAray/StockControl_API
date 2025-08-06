package service;

import java.util.ArrayList;
import model.Usuario;
import model.dao.UsuarioDAO;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dto.ContrasenaDTO;
import model.dto.LoginDTO;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servicio que maneja la lógica de negocio relacionada con usuarios.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos() 
 * - obtenerUsuario(int id)
 * - crearUsuario(Usuario usuario)
 * - login(Usuario loginDTO)
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
     * Retorna todos los usuarios registrados en la base de datos.
     *
     * @return Lista de usuarios o error si no hay resultados.
     */
    public Response obtenerTodosAdministrativos() {
        // Obtiene la lista de usuarios desde el DAO
        List<Usuario> usuarios = dao.getAll().stream().filter(usuario -> usuario.getRol_id() == 2).toList();

        // Verifica si la lista está vacía
        if (usuarios.isEmpty()) {
            // Retorna un error si no se encontraron usuarios
            return ResponseProvider.error("No se encontraron usuarios administrativos", 404);
        }                

        // Retorna la lista de usuarios si se encontraron
        return ResponseProvider.success(usuarios, "Usuarios administrativos obtenidos correctamente", 200);
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
            return ResponseProvider.error("Este correo ya fue registrado", 409);
        }

        // Validar que no exista un usuario con el mismo documento
        Usuario usuarioExistenteDoc = dao.getByDocumento(usuario.getDocumento());
        if (usuarioExistenteDoc != null) {
            // Retorna un error si el documento ya está registrado
            return ResponseProvider.error("Este número de documento ya fue registrado", 409);
        }
        
        // Si no se especifica un rol, se asigna el rol por defecto (2 = Usuario Corriente)
        if (usuario.getRol_id() == 0) usuario.setRol_id(3);        
        
        // Si no vino ficha_id, se le asigna 1 por defecto (No aplica)
        if (usuario.getFicha_id() == 0) usuario.setFicha_id(1);                        
        
        System.out.println(usuario.getContrasena());
        
        String contrasenaHasheada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        usuario.setContrasena(contrasenaHasheada);
        
        System.out.println(usuario.getContrasena());

        // Intentar crear el usuario en la base de datos
        Usuario nuevoUsuario = dao.create(usuario);
        if (nuevoUsuario != null) {            
            nuevoUsuario.setActivo(true);
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
                return ResponseProvider.error("Este número de documento ya fue registrado", 409);
            }

        for (Usuario usuarioRegistrado : usuariosRegistrados)
            if (usuarioRegistrado.getCorreo()== usuario.getCorreo()){
                return ResponseProvider.error("Este correo ya fue registrado", 409);
            }
      
        if (usuario.getRol_id() == 0) {
            usuario.setRol_id(usuarioExistente.getRol_id());
        }

        // Intentar actualizar el usuario en la base de datos
        Usuario usuarioActualizado = dao.update(id, usuario);
        
        if (usuarioActualizado != null) {
            usuarioActualizado.setActivo(usuarioExistente.isActivo());
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

    /**
    * Método que maneja la lógica de autenticación de un usuario.
    * 
    * Este método recibe un objeto DTO con los datos del intento de inicio de sesión,
    * busca al usuario en la base de datos por su documento, y luego verifica si la 
    * contraseña y el rol coinciden con los datos registrados. 
    * 
    * Utiliza la biblioteca BCrypt para verificar contraseñas de forma segura.
    *
    * Posibles respuestas:
    * - 200: Inicio de sesión exitoso.
    * - 401: Contraseña incorrecta o rol no autorizado.
    * - 404: Usuario no encontrado.
    *
    * @param loginDatos Objeto LoginDTO que contiene:
    *                   - documento: Identificador del usuario.
    *                   - contrasena: Contraseña ingresada por el usuario (texto plano).    
    * @return Response con mensaje y código de estado HTTP indicando el resultado del login.
    */
    public Response login(LoginDTO loginDatos) {
        // Busca al usuario en la base de datos usando su documento
        Usuario usuario = dao.getByDocumento(loginDatos.getDocumento());

        // Si no se encuentra el usuario, retorna un error 404 (no encontrado)
        if (usuario == null || !usuario.isActivo()) {
            return ResponseProvider.error("Usuario no encontrado", 404);
        }

        // Verifica que la contraseña proporcionada coincida con el hash almacenado
        boolean contraseñaCorrecta = BCrypt.checkpw(loginDatos.getContrasena(), usuario.getContrasena());                

        // Si la contraseña no coincide, devuelve un error 401 (no autorizado)
        if (!contraseñaCorrecta) {
            return ResponseProvider.error("Contraseña incorrecta", 401);
        }
                
        return ResponseProvider.success(usuario, "Inicio de sesión exitoso", 200);
    }

    /**
     * Cambia la contraseña de un usuario si la actual es válida.
     * 
     * @param id ID del usuario
     * @param dto Objeto que contiene la contraseña actual y la nueva
     * @return Respuesta con estado y mensaje
     */
    public Response cambiarContrasena(int id, ContrasenaDTO dto) {
        Usuario usuario = dao.getById(id);        
        if (usuario == null) return ResponseProvider.error("Usuario no encontrado", 404);
                
        // Validar contraseña actual
        if (!BCrypt.checkpw(dto.getContrasena_actual(), usuario.getContrasena()))
            return ResponseProvider.error("La contraseña actual es incorrecta", 401);
                        
        // Hash y guardar nueva contraseña
        String nuevaHash = BCrypt.hashpw(dto.getContrasena_nueva(), BCrypt.gensalt());         
                
        if (dao.updatePassword(id, nuevaHash)) {
            return ResponseProvider.success(null, "Contraseña actualizada correctamente", 200);
        } else {
            return ResponseProvider.error("Error al actualizar la contraseña", 400);
        }
    }
    
    /**
     * Desactiva la cuenta del usuario si la contraseña ingresada es correcta.
     * 
     * @param id ID del usuario
     * @param dto Objeto que contiene la contraseña actual
     * @return Respuesta con estado y mensaje
     */
    public Response desactivarCuenta(int id, ContrasenaDTO dto) {
        Usuario usuario = dao.getById(id);
        if (usuario == null) return ResponseProvider.error("Usuario no encontrado", 404);

        // Validar contraseña actual
        if (!BCrypt.checkpw(dto.getContrasena_actual(), usuario.getContrasena()))
            return ResponseProvider.error("Contraseña incorrecta", 401);
        
        boolean desactivado = dao.updateState(id, false);
        
        if (desactivado) {
            return ResponseProvider.success(null, "Cuenta desactivada correctamente", 200);
        } else {
            return ResponseProvider.error("Error al desactivar la cuenta", 400);
        }
    }
    /**
     * @param id ID del usuario
     * @param rol Rol del usuario
     * @param estado Estado al que lo desea cambiar
     * @return Respuesta con estado y mensaje
     */
    public Response cambiarEstado(int id, int rol, boolean estado) {
        Usuario usuario = dao.getById(id);
        if (usuario == null) return ResponseProvider.error("Usuario no encontrado", 404);

        if (rol != 1) return ResponseProvider.error("No cuenta con los permisos para realizar esta solicitud", 400);
        
        boolean cambiado = dao.updateState(id, estado);
        
        if (cambiado) {
            return ResponseProvider.success(null, "Estado cambiado correctamente", 200);
        } else {
            return ResponseProvider.error("Error al cambiar el estado de la cuenta", 400);
        }
    }
}
