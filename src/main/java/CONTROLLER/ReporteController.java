package controller;

import java.util.List;
import model.Reporte;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.dao.FotoDAO;
import model.dao.ReporteDAO;
import model.Foto;
import model.dao.ElementoDAO;
import model.dao.UsuarioDAO;

/**
 * Controlador REST para gestionar operaciones relacionadas con los reportes.
 * Este controlador actúa como capa única, fusionando la lógica del servicio directamente.
 * Define rutas HTTP para consultar, crear, actualizar y eliminar reportes.
 *
 * Rutas disponibles:
 * - GET /reportes
 * - GET /reportes/{id}
 * - GET /reportes/inventario/{id}
 * - POST /reportes
 * - PUT /reportes/{id}
 * - DELETE /reportes/{id}
 *
 * @author Yariangel Aray
 */
@Path("/reportes")
public class ReporteController {

    // DAO que maneja el acceso a los datos de los reportes y a las fotos
    private final ReporteDAO dao;
    private final FotoDAO fotoDao;
    private final UsuarioDAO usuarioDao;
    private final ElementoDAO elementoDao;

    /**
     * Constructor del controlador. Inicializa el DAO de reportes.
     */
    public ReporteController() {
        // Se instancia el DAO de reportes
        this.dao = new ReporteDAO();
        this.fotoDao = new FotoDAO();
        this.usuarioDao = new UsuarioDAO();
        this.elementoDao = new ElementoDAO();
    }

    /**
     * Obtiene todos los reportes registrados en el sistema, incluyendo sus fotos asociadas.
     *
     * @return Respuesta HTTP con lista de reportes y sus fotos (código 200),
     *         o error 404 si no hay datos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            List<Reporte> reportes = dao.getAll();
            if (reportes.isEmpty()) return ResponseProvider.error("No se encontraron reportes", 404);

            // Asignar fotos a cada reporte
            for (Reporte reporte : reportes) {
                List<Foto> fotos = fotoDao.getAllByIdReporte(reporte.getId());
                reporte.setFotos(fotos);
            }

            return ResponseProvider.success(reportes, "Reportes obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace(); // Mostrar traza de error
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene todos los reportes de un inventario específico, incluyendo sus fotos.
     *
     * @param idInventario ID del inventario cuyos reportes se desean consultar.
     * @return Lista de reportes con fotos (código 200), 204 si no hay, o 500 si hay error.
     */
    @GET
    @Path("/inventario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerReportesPorInventario(@PathParam("id") int idInventario) {
        try {
            List<Reporte> reportes = dao.getAllByIdInventario(idInventario);
            if (reportes == null || reportes.isEmpty())
                return ResponseProvider.error("No se encontraron reportes para este inventario", 204);

            // Concatenar fotos a cada reporte
            for (Reporte reporte : reportes) {
                List<Foto> fotos = fotoDao.getAllByIdReporte(reporte.getId());
                reporte.setFotos(fotos);
            }

            return ResponseProvider.success(reportes, "Reportes obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un único reporte por su ID, incluyendo sus fotos.
     *
     * @param id ID del reporte.
     * @return Reporte con sus fotos (código 200) o error 404 si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerReporte(@PathParam("id") int id) {
        try {
            Reporte reporte = dao.getById(id);
            if (reporte == null) return ResponseProvider.error("Reporte no encontrado", 404);

            // Obtener y asignar fotos
            List<Foto> fotos = fotoDao.getAllByIdReporte(id);
            reporte.setFotos(fotos);

            return ResponseProvider.success(reporte, "Reporte obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo reporte.
     *
     * @param reporte Objeto JSON con los datos del reporte a registrar.
     * @return Respuesta con el reporte creado (código 201),
     *         error 400 si falló la creación, o 500 si ocurrió una excepción.
     */
    @POST
    @ValidarCampos(entidad = "reporte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearReporte(Reporte reporte) {
        try {
            // Validar que el usuario exista
            if (usuarioDao.getById(reporte.getUsuario_id()) == null) {
                return ResponseProvider.error("El usuario que genera el reporte no existe.", 404);
            }

            // Validar que el elemento exista
            if (elementoDao.getById(reporte.getElemento_id()) == null) {
                return ResponseProvider.error("El elemento reportado no existe.", 404);
            }
            
            Reporte nuevoReporte = dao.create(reporte); // Insertar en BD
            Reporte creado = dao.getById(nuevoReporte.getId());
            if (nuevoReporte != null && creado != null)
                return ResponseProvider.success(creado, "Reporte creado correctamente", 201);
            return ResponseProvider.error("Error al crear el reporte", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza los datos de un reporte existente.
     *
     * @param id      ID del reporte a actualizar.
     * @param reporte Objeto JSON con los datos actualizados del reporte.
     * @return Respuesta con el reporte actualizado (código 200),
     *         error 404 si no se encuentra, o 500 si ocurre una excepción.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "reporte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarReporte(@PathParam("id") int id, Reporte reporte) {
        try {
            Reporte existente = dao.getById(id); // Validar existencia
            if (existente == null)
                return ResponseProvider.error("Reporte no encontrado", 404);
            
            // Validar que el usuario exista
            if (usuarioDao.getById(reporte.getUsuario_id()) == null) {
                return ResponseProvider.error("El usuario que genera el reporte no existe.", 404);
            }

            // Validar que el elemento exista
            if (elementoDao.getById(reporte.getElemento_id()) == null) {
                return ResponseProvider.error("El elemento reportado no existe.", 404);
            }

            Reporte actualizado = dao.update(id, reporte); // Ejecutar actualización
            if (actualizado != null)
                return ResponseProvider.success(actualizado, "Reporte actualizado correctamente", 200);
            return ResponseProvider.error("Error al actualizar el reporte", 404);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un reporte solo si no tiene fotos asociadas.
     *
     * @param id ID del reporte a eliminar.
     * @return Respuesta con código 200 si fue eliminado,
     *         409 si tiene fotos asociadas, 404 si no existe, o 500 si ocurre un error.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarReporte(@PathParam("id") int id) {
        try {
            Reporte existente = dao.getById(id); // Verificar existencia
            if (existente == null)
                return ResponseProvider.error("Reporte no encontrado", 404);

            FotoDAO fotoDao = new FotoDAO(); // DAO de fotos
            List<Foto> fotos = fotoDao.getAllByIdReporte(id); // Consultar fotos del reporte

            if (fotos != null && !fotos.isEmpty())
                return ResponseProvider.error("No se puede eliminar el reporte porque tiene fotos asociadas", 409);

            boolean eliminado = dao.delete(id); // Ejecutar eliminación
            if (eliminado)
                return ResponseProvider.success(null, "Reporte eliminado correctamente", 200);
            return ResponseProvider.error("Error al eliminar el reporte", 500);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
