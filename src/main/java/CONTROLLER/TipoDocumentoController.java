package controller;

import model.dao.TipoDocumentoDAO;
import model.dao.UsuarioDAO;
import model.TipoDocumento;
import model.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los tipos de documento.
 *
 * Rutas disponibles:
 * - GET /tipos-documento: Listar todos los tipos.
 * - GET /tipos-documento/{id}: Buscar tipo por ID.
 * - POST /tipos-documento: Crear nuevo tipo.
 * - PUT /tipos-documento/{id}: Actualizar tipo existente.
 * - DELETE /tipos-documento/{id}: Eliminar tipo. 
 *
 * @author Yariangel Aray
 */
@Path("/tipos-documento") // Define la ruta base para este controlador
public class TipoDocumentoController {

    // DAO para el acceso a la base de datos de tipos de documento
    private final TipoDocumentoDAO tipoDAO;

    // DAO de usuarios, usado para verificar si hay usuarios asociados antes de eliminar un tipo
    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor del controlador.
     * Instancia los DAOs necesarios para operar.
     */
    public TipoDocumentoController() {
        tipoDAO = new TipoDocumentoDAO();
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Obtiene todos los tipos registrados en el sistema.
     *
     * @return Lista de tipos (200) o mensaje de error si no hay datos (404).
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Consulta todos los tipos desde la base de datos
            List<TipoDocumento> tipos = tipoDAO.getAll();

            // Si no se encuentran registros, retorna 404
            if (tipos.isEmpty()) {
                return ResponseProvider.error("No se encontraron tipos de documento", 404);
            }

            // Retorna la lista de tipos con éxito
            return ResponseProvider.success(tipos, "Tipos de documento obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error para depuración
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un tipo por su ID único.
     *
     * @param id Identificador del tipo.
     * @return Tipo encontrado (200) o mensaje de error si no existe (404).
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTipo(@PathParam("id") int id) {
        try {
            // Buscar tipo de documento por ID
            TipoDocumento tipo = tipoDAO.getById(id);

            // Validar si se encontró
            if (tipo == null) {
                return ResponseProvider.error("Tipo de documento no encontrado", 404);
            }

            // Retornar tipo con éxito
            return ResponseProvider.success(tipo, "Tipo de documento obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo tipo en el sistema.     
     *
     * @param tipo Objeto TipoDocumento recibido en el cuerpo de la petición.
     * @return Respuesta con el tipo creado (201) o error si ocurre un fallo (400).
     */
    @POST
    @ValidarCampos(entidad = "tipo_documento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearTipo(TipoDocumento tipo) {
        try {
            // Crear el tipo de documento
            TipoDocumento nuevoTipo = tipoDAO.create(tipo);

            // Validar si se creó correctamente
            if (nuevoTipo != null) {
                return ResponseProvider.success(nuevoTipo, "Tipo de documento creado correctamente", 201);
            } else {
                return ResponseProvider.error("Error al crear el tipo de documento", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza la información de un tipo existente.     
     *
     * @param id   ID del tipo a actualizar.
     * @param tipo Datos nuevos del tipo.
     * @return Tipo actualizado (200) o error si no existe o falla (404).
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "tipo_documento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarTipo(@PathParam("id") int id, TipoDocumento tipo) {
        try {
            // Verificar si el tipo de documento existe
            TipoDocumento tipoExistente = tipoDAO.getById(id);
            if (tipoExistente == null) {
                return ResponseProvider.error("Tipo de documento no encontrado", 404);
            }

            // Actualizar tipo de documento
            TipoDocumento tipoActualizado = tipoDAO.update(id, tipo);

            // Validar si fue exitoso
            if (tipoActualizado != null) {
                return ResponseProvider.success(tipoActualizado, "Tipo de documento actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el tipo de documento", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un tipo del sistema mediante su ID.
     * Solo se permite si no hay usuarios asociados a ese tipo de documento.
     *
     * @param id ID del tipo a eliminar.
     * @return Respuesta de éxito (200), conflicto si hay usuarios (409), o error (404, 500).
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarTipo(@PathParam("id") int id) {
        try {
            // Verificar existencia del tipo de documento
            TipoDocumento tipoExistente = tipoDAO.getById(id);
            if (tipoExistente == null) {
                return ResponseProvider.error("Tipo de documento no encontrado", 404);
            }

            // Verificar si hay usuarios que usan este tipo
            List<Usuario> usuarios = usuarioDAO.getAllByIdTipoDocumento(id);
            if (usuarios != null && !usuarios.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el tipo de documento porque tiene usuarios asociados", 409);
            }

            // Intentar eliminar el tipo
            boolean eliminado = tipoDAO.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Tipo de documento eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el tipo de documento", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
