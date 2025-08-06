package controller;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Ficha;
import model.Genero;
import model.Rol;
import model.TipoDocumento;

import model.Usuario;
import model.dao.FichaDAO;
import model.dao.GeneroDAO;
import model.dao.RolDAO;
import model.dao.TipoDocumentoDAO;
import model.dao.UsuarioDAO;
import model.dto.ContrasenaDTO;
import model.dto.LoginDTO;


import org.mindrot.jbcrypt.BCrypt;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar usuarios.
 * 
 * 
 * Rutas disponibles:
 * 
 * - GET /usuarios: Listar todos los usuarios
 * - GET /usuarios/administrativos: Listar usuarios administrativos
 * - GET /usuarios/{id}: Buscar usuario por ID
 * - POST /usuarios: Crear nuevo usuario
 * - POST /usuarios/login: Autenticar usuario
 * - PUT /usuarios/{id}: Actualizar usuario existente
 * - PUT /usuarios/{id}/contrasena: Cambiar contraseña
 * - PUT /usuarios/{id}/desactivar: Desactivar cuenta
 * - PUT /usuarios/{id}/estado/{estado}/permiso/{rol}: Cambiar estado de cuenta
 * - DELETE /usuarios/{id}: Eliminar usuario
 * 
 * 
 * @author Yariangel Aray
 */
@Path("/usuarios") // Define la ruta base para este controlador REST
public class UsuarioController{    
    
    /**
     * Instancia del DAO (Data Access Object) para acceder directamente a la base de datos.     
     */
    private UsuarioDAO dao;    
        
    private FichaDAO fichaDao;
    private RolDAO rolDao;
    private GeneroDAO generoDao;
    private TipoDocumentoDAO tipoDocumentoDao;
        
    /**
     * Constructor que inicializa el DAO para el acceso a datos.     
     */
    public UsuarioController() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new UsuarioDAO();
        fichaDao = new FichaDAO();
        rolDao = new RolDAO();
        generoDao = new GeneroDAO();
        tipoDocumentoDao = new TipoDocumentoDAO();
    }    

    /**   
     * Método que obtiene todos los usuarios registrados en el sistema.          
     * 
     * @return Response con lista de usuarios (200) o error (404/500)
     */
    @GET // Método HTTP GET para consultar recursos
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {                        
            // Obtiene la lista completa de usuarios desde el DAO
            List<Usuario> usuarios = dao.getAll();

            // Verifica si la lista está vacía (sin usuarios registrados)
            if (usuarios.isEmpty()) {
                // Retorna un error 404 si no se encontraron usuarios
                return ResponseProvider.error("No se encontraron usuarios", 404);
            }

            // Retorna la lista de usuarios con código 200 (éxito) si se encontraron datos
            return ResponseProvider.success(usuarios, "Usuarios obtenidos correctamente", 200);
            
        } catch (Exception e) {                       
            // Imprime el stack trace del error en la consola para debugging
            e.printStackTrace();
            // Retorna un error 500 (error interno del servidor) si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**              
     * Método que obtiene únicamente los usuarios con rol administrativo (rol_id = 2).
     * Filtra los usuarios por su rol antes de devolverlos.     
     * 
     * @return Response con usuarios administrativos (200) o error (404/500)
     */
    @GET // Método HTTP GET para consultar recursos
    @Path("/administrativos") // Sub-ruta específica para usuarios administrativos
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en formato JSON
    public Response obtenerTodosAdministrativos() {
        try {                       
            // Obtiene todos los usuarios y filtra solo los que tienen rol_id = 2 (administrativos)
            List<Usuario> usuarios = dao.getAll().stream()
                .filter(usuario -> usuario.getRol_id() == 2) // Filtra por rol administrativo
                .toList(); // Convierte el stream filtrado a lista

            // Verifica si no se encontraron usuarios administrativos
            if (usuarios.isEmpty()) {
                // Retorna error 404 con mensaje específico para usuarios administrativos
                return ResponseProvider.error("No se encontraron usuarios administrativos", 404);
            }

            // Retorna la lista filtrada de usuarios administrativos
            return ResponseProvider.success(usuarios, "Usuarios administrativos obtenidos correctamente", 200);
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**          
     * Método que busca y retorna un usuario específico por su ID único.
     * 
     * @param id Identificador único del usuario a buscar
     * @return Response con usuario encontrado (200) o error (404/500)
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del usuario como parámetro de path
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("id") int id) {
        try {                       
            // Busca el usuario específico por su ID en la base de datos
            Usuario usuario = dao.getById(id);

            // Verifica si el usuario fue encontrado
            if (usuario == null) {
                // Retorna error 404 si no se encontró el usuario con ese ID
                return ResponseProvider.error("Usuario no encontrado", 404);
            }

            // Retorna el usuario encontrado con mensaje de éxito
            return ResponseProvider.success(usuario, "Usuario obtenido correctamente", 200);
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**          
     * Método que registra un nuevo usuario en el sistema.
     * Valida que no existan duplicados de correo y documento antes de crear.
     * Encripta la contraseña y asigna valores por defecto para rol y ficha por si no vienen en el json.
     * 
     * 
     * @param usuario Objeto Usuario con los datos del nuevo registro
     * @return Response con usuario creado (201) o error (409/400/500)
     */
    @POST // Método HTTP POST para crear recursos
    @ValidarCampos(entidad = "usuario") // Middleware que valida los campos del usuario
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en formato JSON
    public Response crearUsuario(Usuario usuario) {
        try {                        
            // Verificar que no exista un usuario con el mismo correo
            Usuario usuarioExistenteCorreo = dao.getByCorreo(usuario.getCorreo());
            if (usuarioExistenteCorreo != null) {
                // Retorna error 409 (conflicto) si el correo ya está registrado
                return ResponseProvider.error("Este correo ya fue registrado", 409);
            }

            // Verificar que no exista un usuario con el mismo documento 
            Usuario usuarioExistenteDoc = dao.getByDocumento(usuario.getDocumento());
            if (usuarioExistenteDoc != null) {
                // Retorna error 409 (conflicto) si el documento ya está registrado
                return ResponseProvider.error("Este número de documento ya fue registrado", 409);
            }           
            
            // Validar existencia: tipo de documento
            TipoDocumento tipoDoc = tipoDocumentoDao.getById(usuario.getTipo_documento_id());
            if (tipoDoc == null) {
                return ResponseProvider.error("El tipo de documento especificado no existe.", 404);
            }

            // Validar existencia: género
            Genero genero = generoDao.getById(usuario.getGenero_id());
            if (genero == null) {
                return ResponseProvider.error("El género especificado no existe.", 404);
            }

            // Validar existencia: ficha (si se envió una distinta de 1)
            if (usuario.getFicha_id() != 0 && usuario.getFicha_id() != 1) {
                Ficha ficha = fichaDao.getById(usuario.getFicha_id());
                if (ficha == null) {
                    return ResponseProvider.error("La ficha especificada no existe.", 404);
                }
            } else {
                // Si no se envía ficha válida, asignar por defecto (1 = No aplica)
                usuario.setFicha_id(1);
            }

            // Validar existencia: rol (si se especificó uno)
            if (usuario.getRol_id() != 0) {
                Rol rol = rolDao.getById(usuario.getRol_id());
                if (rol == null) {
                    return ResponseProvider.error("El rol especificado no existe.", 404);
                }
            } else {
                // Asignar rol por defecto (3 = Aprendiz / Usuario corriente)
                usuario.setRol_id(3);
            }
                     
            
            // Genera un hash seguro de la contraseña usando BCrypt con salt automático
            String contrasenaHasheada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            // Reemplaza la contraseña en texto plano por el hash
            usuario.setContrasena(contrasenaHasheada);                       
            
            // Intenta crear el usuario en la base de datos a través del DAO
            Usuario nuevoUsuario = dao.create(usuario);
            
            if (nuevoUsuario != null) {
                // Si la creación fue exitosa, marca al usuario como activo
                nuevoUsuario.setActivo(true);
                // Retorna el nuevo usuario creado con código 201 (creado)
                return ResponseProvider.success(nuevoUsuario, "Usuario creado correctamente", 201);
            } else {
                // Retorna error 400 si hubo un problema al crear el usuario en la BD
                return ResponseProvider.error("Error al crear el usuario", 400);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**          
     * EMétodo actualiza los datos de un usuario existente.
     * Valida que no existan duplicados de correo y documento (excluyendo el usuario actual).
     * Preserva el rol y estado activo si no se especifican nuevos valores.
     * 
     * 
     * @param id Identificador del usuario a actualizar
     * @param usuario Objeto con los nuevos datos del usuario
     * @return Response con usuario actualizado (200) o error (404/409/500)
     */
    @PUT // Método HTTP PUT para actualizar recursos completos
    @Path("/{id}") // Ruta que incluye el ID del usuario a actualizar
    @ValidarCampos(entidad = "usuario") // Validación de campos antes de procesar
    @Consumes(MediaType.APPLICATION_JSON) // Cuerpo de petición en JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response actualizarUsuario(@PathParam("id") int id, Usuario usuario) {
        try {                        
            // Verificar que el usuario existe
            Usuario usuarioExistente = dao.getById(id);
            if (usuarioExistente == null) {
                // Retorna error 404 si el usuario no fue encontrado
                return ResponseProvider.error("Usuario no encontrado", 404);
            }

            // Verificar duplicados excluyendo el usuario actual            
            // Obtiene todos los usuarios de la base de datos
            List<Usuario> usuarios = dao.getAll();
            // Crea una lista de usuarios registrados excluyendo el usuario que se está actualizando
            List<Usuario> usuariosRegistrados = new ArrayList<>();

            // Filtra todos los usuarios excepto el que se está actualizando
            for (Usuario usuarioRegistrado : usuarios) {
                if (usuarioRegistrado.getId() != id) {
                    usuariosRegistrados.add(usuarioRegistrado);
                }
            }

            // Verifica que el nuevo documento no esté en uso por otro usuario
            for (Usuario usuarioRegistrado : usuariosRegistrados) {
                if (usuarioRegistrado.getDocumento() == usuario.getDocumento()) {
                    return ResponseProvider.error("Este número de documento ya fue registrado", 409);
                }
            }

            // Verifica que el nuevo correo no esté en uso por otro usuario
            for (Usuario usuarioRegistrado : usuariosRegistrados) {
                if (usuarioRegistrado.getCorreo().equals(usuario.getCorreo())) {
                    return ResponseProvider.error("Este correo ya fue registrado", 409);
                }
            }    
            
            // Validar existencia: tipo de documento
            TipoDocumento tipoDoc = tipoDocumentoDao.getById(usuario.getTipo_documento_id());
            if (tipoDoc == null) {
                return ResponseProvider.error("El tipo de documento especificado no existe.", 404);
            }

            // Validar existencia: género
            Genero genero = generoDao.getById(usuario.getGenero_id());
            if (genero == null) {
                return ResponseProvider.error("El género especificado no existe.", 404);
            }

            Ficha ficha = fichaDao.getById(usuario.getFicha_id());
            if (ficha == null) {
                return ResponseProvider.error("La ficha especificada no existe.", 404);
            }           
            
            // Si no se especifica un nuevo rol, mantiene el rol actual
            if (usuario.getRol_id() == 0) {
                usuario.setRol_id(usuarioExistente.getRol_id());
            }            
            
            // Intenta actualizar el usuario en la base de datos
            Usuario usuarioActualizado = dao.update(id, usuario);
            
            if (usuarioActualizado != null) {
                // Preserva el estado activo del usuario original
                usuarioActualizado.setActivo(usuarioExistente.isActivo());
                // Retorna el usuario actualizado con mensaje de éxito
                return ResponseProvider.success(usuarioActualizado, "Usuario actualizado correctamente", 200);
            } else {
                // Retorna error 404 si hubo un problema al actualizar
                return ResponseProvider.error("Error al actualizar el usuario", 404);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Método que elimina permanentemente un usuario del sistema.
     * Verifica que el usuario exista antes de proceder con la eliminación.
     *
     * @param id Identificador del usuario a eliminar
     * @return Response con mensaje de éxito (200) o error (404/500)
     */
    @DELETE // Método HTTP DELETE para eliminar recursos
    @Path("/{id}") // Ruta que incluye el ID del usuario a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response eliminarUsuario(@PathParam("id") int id) {
        try {                        
            // Verificar existencia del usuario ---
            Usuario usuarioExistente = dao.getById(id);
            if (usuarioExistente == null) {
                // Retorna error 404 si el usuario no fue encontrado
                return ResponseProvider.error("Usuario no encontrado", 404);
            }           
            
            // Intenta eliminar el usuario de la base de datos
            boolean eliminado = dao.delete(id);
            
            if (eliminado) {
                // Retorna mensaje de éxito si el usuario fue eliminado correctamente
                return ResponseProvider.success(null, "Usuario eliminado correctamente", 200);
            } else {
                // Retorna error 500 si hubo un problema al eliminar el usuario
                return ResponseProvider.error("Error al eliminar el usuario", 500);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**          
     * Método que maneja la autenticación de usuarios en el sistema.
     * Verifica el documento, contraseña y estado activo del usuario.
     * Utiliza BCrypt para verificar contraseñas de forma segura.
     * 
     * Posibles respuestas:
     * - 200: Inicio de sesión exitoso
     * - 401: Contraseña incorrecta
     * - 404: Usuario no encontrado o inactivo
     * - 500: Error interno del servidor
     * 
     * @param loginDatos DTO con documento y contraseña del usuario
     * @return Response con datos del usuario (200) o error (401/404/500)
     */
    @POST // Método HTTP POST para procesar autenticación
    @Path("/login") // Sub-ruta específica para login
    @Consumes(MediaType.APPLICATION_JSON) // Cuerpo de petición en JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response login(LoginDTO loginDatos) {
        try {            
            // Busca al usuario en la base de datos usando su documento como identificador
            Usuario usuario = dao.getByDocumento(loginDatos.getDocumento());

            // Verifica si el usuario existe y está activo
            if (usuario == null || !usuario.isActivo()) {
                // Retorna error 404 si el usuario no existe o está inactivo
                return ResponseProvider.error("Usuario no encontrado", 404);
            }
            
            // Utiliza BCrypt para verificar que la contraseña ingresada coincida con el hash almacenado
            boolean contraseñaCorrecta = BCrypt.checkpw(loginDatos.getContrasena(), usuario.getContrasena());

            // Si la contraseña no coincide, devuelve error de autenticación
            if (!contraseñaCorrecta) {
                return ResponseProvider.error("Contraseña incorrecta", 401);
            }           
            
            // Si llegó aquí, la autenticación fue exitosa
            return ResponseProvider.success(usuario, "Inicio de sesión exitoso", 200);
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**          
     * Método que permite a un usuario cambiar su contraseña.
     * Requiere verificación de la contraseña actual antes de establecer la nueva.
     * La nueva contraseña se encripta con BCrypt antes de almacenarla.
     * 
     * @param id Identificador del usuario
     * @param dto DTO con contraseña actual y nueva contraseña
     * @return Response con mensaje de éxito (200) o error (404/401/400/500)
     */
    @PUT
    @Path("/{id}/contrasena") // Sub-ruta específica para cambio de contraseña
    @Consumes(MediaType.APPLICATION_JSON) // Cuerpo de petición en JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response cambiarContrasena(@PathParam("id") int id, ContrasenaDTO dto) {
        try {            
            
            // Verifica si el usuario existe
            Usuario usuario = dao.getById(id);
            if (usuario == null) {
                return ResponseProvider.error("Usuario no encontrado", 404);
            }            
            
            // Verifica que la contraseña actual proporcionada sea correcta
            if (!BCrypt.checkpw(dto.getContrasena_actual(), usuario.getContrasena())) {
                return ResponseProvider.error("La contraseña actual es incorrecta", 401);
            }           
            
            // Genera un hash seguro para la nueva contraseña
            String nuevaHash = BCrypt.hashpw(dto.getContrasena_nueva(), BCrypt.gensalt());

            // Intenta actualizar la contraseña en la base de datos
            if (dao.updatePassword(id, nuevaHash)) {
                return ResponseProvider.success(null, "Contraseña actualizada correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar la contraseña", 400);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Método que permite desactivar la cuenta de un usuario.
     * Requiere verificación de contraseña como medida de seguridad.
     * El usuario queda inactivo pero no se elimina de la base de datos.
     * 
     * 
     * @param id Identificador del usuario a desactivar
     * @param dto DTO con contraseña actual para verificación
     * @return Response con mensaje de éxito (200) o error (404/401/400/500)
     */
    @PUT
    @Path("/{id}/desactivar") // Sub-ruta específica para desactivación
    @Consumes(MediaType.APPLICATION_JSON) // Cuerpo de petición en JSON
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response desactivarCuenta(@PathParam("id") int id, ContrasenaDTO dto) {
        try {                        
            // Verifica si el usuario exista
            Usuario usuario = dao.getById(id);
            if (usuario == null) {
                return ResponseProvider.error("Usuario no encontrado", 404);
            }           
            
            // Verifica que la contraseña proporcionada sea correcta antes de desactivar
            if (!BCrypt.checkpw(dto.getContrasena_actual(), usuario.getContrasena())) {
                return ResponseProvider.error("Contraseña incorrecta", 401);
            }           
            
            // Cambia el estado del usuario a inactivo (false)
            boolean desactivado = dao.updateState(id, false);

            if (desactivado) {
                return ResponseProvider.success(null, "Cuenta desactivada correctamente", 200);
            } else {
                return ResponseProvider.error("Error al desactivar la cuenta", 400);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * ============================= CAMBIAR ESTADO DE CUENTA (ADMIN) =============================
     * 
     * Endpoint REST que permite a un superadministrador cambiar el estado de cualquier cuenta.
     * Requiere permisos de superadministrador (rol = 1) para ejecutar esta acción.
     * Permite activar o desactivar cuentas de usuario sin requerir contraseña.     
     * 
     * @param id Identificador del usuario cuyo estado se cambiará
     * @param rol Rol del usuario que ejecuta la acción (debe ser 1 para autorizar)
     * @param estado Nuevo estado para la cuenta (true = activo, false = inactivo)
     * @return Response con mensaje de éxito (200) o error (404/400/500)
     */
    @PUT
    @Path("/{id}/estado/{estado}/permiso/{rol}") // Ruta con múltiples parámetros
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    public Response cambiarEstado(@PathParam("id") int id, @PathParam("rol") int rol, @PathParam("estado") boolean estado) {
        try {                        
            // Verifica si el usuario exista
            Usuario usuario = dao.getById(id);
            if (usuario == null) {
                return ResponseProvider.error("Usuario no encontrado", 404);
            }           
            
            // Verifica que quien ejecuta la acción tenga rol de superadministrador (rol = 1)
            if (rol != 1) {
                return ResponseProvider.error("No cuenta con los permisos para realizar esta solicitud", 400);
            }           
            
            // Actualiza el estado del usuario según el parámetro recibido
            boolean cambiado = dao.updateState(id, estado);

            if (cambiado) {
                // Determina el mensaje según el estado aplicado
                String mensaje = estado ? "Cuenta activada correctamente" : "Cuenta desactivada correctamente";
                return ResponseProvider.success(null, mensaje, 200);
            } else {
                return ResponseProvider.error("Error al cambiar el estado de la cuenta", 400);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}