package controller;

import model.dao.FichaDAO;
import model.dao.UsuarioDAO;
import model.Ficha;
import model.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import model.ProgramaFormacion;
import model.dao.ProgramaFormacionDAO;

/**
 * Controlador REST que también contiene la lógica de negocio para gestionar fichas.
 * Permite consultar, crear, actualizar y eliminar fichas, asegurando las reglas de negocio.
 *
 * Rutas disponibles:
 * - GET /fichas
 * - GET /fichas/{id}
 * - POST /fichas
 * - PUT /fichas/{id}
 * - DELETE /fichas/{id}
 *
 * Antes de eliminar una ficha, se valida que no existan usuarios asociados.
 *
 * @author Yariangel Aray
 */
@Path("/fichas")
public class FichaController {

    // DAO que maneja operaciones de base de datos para fichas
    private final FichaDAO fichaDao;
    private final ProgramaFormacionDAO programaDao;

    // Constructor que instancia el DAO
    public FichaController() {
        this.fichaDao = new FichaDAO();
        this.programaDao = new ProgramaFormacionDAO();
    }

    /**
     * Obtiene todas las fichas registradas en el sistema.
     *
     * @return Lista de fichas o mensaje de error si no hay resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodas() {
        try {
            // Se obtiene la lista completa de fichas
            List<Ficha> fichas = fichaDao.getAll();

            // Si no hay fichas, se retorna error 404
            if (fichas.isEmpty()) {
                return ResponseProvider.error("No se encontraron fichas registradas", 404);
            }

            // Retorna lista de fichas con éxito
            return ResponseProvider.success(fichas, "Fichas obtenidas correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime error en consola
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene una ficha por su ID único.
     *
     * @param id ID de la ficha a buscar.
     * @return Ficha encontrada o error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerFicha(@PathParam("id") int id) {
        try {
            // Busca la ficha por ID
            Ficha ficha = fichaDao.getById(id);

            // Si no existe, retorna error
            if (ficha == null) {
                return ResponseProvider.error("Ficha no encontrada", 404);
            }

            // Retorna ficha encontrada
            return ResponseProvider.success(ficha, "Ficha obtenida correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra una nueva ficha en el sistema.
     *
     * @param ficha Objeto ficha recibido desde el cuerpo JSON.
     * @return Respuesta con estado 201 si se creó correctamente.
     */
    @POST
    @ValidarCampos(entidad = "ficha")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearFicha(Ficha ficha) {
        try {
            // Verificamos que el programa de formación exista
            ProgramaFormacion programaExistente = programaDao.getById(ficha.getPrograma_id());
            if (programaExistente == null){
                return ResponseProvider.error("El programa de formación especificado no existe.", 404);
            }
            
            // Intenta crear la ficha
            Ficha nuevaFicha = fichaDao.create(ficha);

            // Verifica si fue creada correctamente
            if (nuevaFicha != null) {
                return ResponseProvider.success(nuevaFicha, "Ficha creada correctamente", 201);
            }

            // Si no se pudo crear, retorna error
            return ResponseProvider.error("Error al crear la ficha", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza los datos de una ficha existente.
     *
     * @param id ID de la ficha a actualizar.
     * @param ficha Nuevos datos de la ficha.
     * @return Respuesta con ficha actualizada o error si no existe.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "ficha")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarFicha(@PathParam("id") int id, Ficha ficha) {
        try {                        
            // Verifica si la ficha existe antes de actualizar
            Ficha fichaExistente = fichaDao.getById(id);
            if (fichaExistente == null) {
                return ResponseProvider.error("Ficha no encontrada", 404);
            }
            
            // Verificamos que el programa de formación exista
            ProgramaFormacion programaExistente = programaDao.getById(ficha.getPrograma_id());
            if (programaExistente == null){
                return ResponseProvider.error("El programa de formación especificado no existe.", 404);
            }

            // Realiza la actualización
            Ficha fichaActualizada = fichaDao.update(id, ficha);

            // Verifica si se actualizó correctamente
            if (fichaActualizada != null) {
                return ResponseProvider.success(fichaActualizada, "Ficha actualizada correctamente", 200);
            }

            // Si falló la actualización
            return ResponseProvider.error("Error al actualizar la ficha", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina una ficha si no tiene usuarios asociados.
     *
     * @param id ID de la ficha a eliminar.
     * @return Resultado indicando si fue eliminada o no.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarFicha(@PathParam("id") int id) {
        try {
            // Verifica que la ficha exista
            Ficha fichaExistente = fichaDao.getById(id);
            if (fichaExistente == null) {
                return ResponseProvider.error("Ficha no encontrada", 404);
            }

            // Verifica si tiene usuarios asociados antes de eliminar
            UsuarioDAO usuarioDao = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDao.getAllByIdFicha(id);

            // Si tiene usuarios, no se permite la eliminación
            if (usuarios != null && !usuarios.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar la ficha porque tiene usuarios asociados", 409);
            }

            // Si no tiene usuarios, intenta eliminar
            boolean eliminada = fichaDao.delete(id);
            if (eliminada) {
                return ResponseProvider.success(null, "Ficha eliminada correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar la ficha", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
