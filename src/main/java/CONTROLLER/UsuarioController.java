package controller;

import middleware.ValidarCampos;
import service.UsuarioService;
import model.entity.Usuario;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar usuarios.
 *
 * Rutas disponibles:
 * - GET /usuarios: Listar todos los usuarios.
 * - GET /usuarios/{id}: Buscar usuario por ID.
 * - GET /usuarios/correo/{correo}: Buscar usuario por correo.
 * - GET /usuarios/documento/{documento}: Buscar usuario por documento.
 * - POST /usuarios: Crear nuevo usuario.
 * - PUT /usuarios/{id}: Actualizar usuario existente.
 * - DELETE /usuarios/{id}: Eliminar usuario.
 *
 * @author Yariangel Aray
 */
@Path("/usuarios") // Define la ruta base para este controlador
public class UsuarioController {

    UsuarioService service; // Instancia del servicio que maneja la lógica de negocio

    public UsuarioController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new UsuarioService();
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Lista de usuarios o mensaje de error si ocurre una excepción.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response obtenerTodos() {
        try {
            // Llama al servicio para obtener todos los usuarios
            return service.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Retorna un error 500 si ocurre una excepción
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un usuario por su ID único.
     *
     * @param id Identificador del usuario.
     * @return Usuario encontrado o mensaje de error si no existe o ocurre una excepción.
     */
    @GET
    @Path("/{id}") // Ruta que incluye el ID del usuario
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener el usuario por ID
            return service.obtenerUsuario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un usuario por su número de documento.
     *
     * @param documento Documento del usuario.
     * @return Usuario encontrado o mensaje de error.
     */
    @GET
    @Path("/documento/{documento}") // Ruta que incluye el documento del usuario
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorDocumento(@PathParam("documento") String documento) {
        try {
            // Llama al servicio para obtener el usuario por documento
            return service.obtenerPorDocumento(documento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario.
     * @return Usuario encontrado o mensaje de error.
     */
    @GET
    @Path("/correo/{correo}") // Ruta que incluye el correo del usuario
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorCorreo(@PathParam("correo") String correo) {
        try {
            // Llama al servicio para obtener el usuario por correo
            return service.obtenerPorCorreo(correo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Se valida el contenido con una clase Middleware (`@ValidarCampos`).
     *
     * @param usuario Objeto Usuario recibido en el cuerpo de la petición.
     * @return Respuesta con estado y mensaje.
     */
    @POST // Método HTTP POST
    @ValidarCampos(entidad = "usuario") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response crearUsuario(Usuario usuario) {
        try {
            // Si no vino ficha_id, se le asigna 1 por defecto
            if (usuario.getFicha_id() == 0) usuario.setFicha_id(1);
                
            // Llama al servicio para crear un nuevo usuario
            return service.crearUsuario(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un usuario existente.
     * Se validan los nuevos campos antes de aplicar los cambios.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Datos nuevos del usuario.
     * @return Respuesta con mensaje de éxito o error.
     */
    @PUT // Método HTTP PUT
    @Path("/{id}") // Ruta que incluye el ID del usuario
    @ValidarCampos(entidad = "usuario") // Anotación que activa la validación de campos
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el cuerpo de la petición es JSON
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response actualizarUsuario(@PathParam("id") int id, Usuario usuario) {
        try {
            // Llama al servicio para actualizar el usuario
            return service.actualizarUsuario(id, usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un usuario del sistema mediante su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Respuesta indicando si la eliminación fue exitosa o no.
     */
    @DELETE // Método HTTP DELETE
    @Path("/{id}") // Ruta que incluye el ID del usuario
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public Response eliminarUsuario(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar el usuario
            return service.eliminarUsuario(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
