package controller;

import utils.ResponseProvider;
import java.io.IOException;
import model.dao.FotoDAO;
import model.Foto;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import model.dao.ReporteDAO;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 * Controlador REST que también contiene la lógica de negocio para gestionar fotos.
 * Permite consultar, crear, y eliminar fotos, tanto desde base de datos como desde el sistema de archivos.
 * 
 * Usa ServletContext para acceder a la ruta física del proyecto desplegado y guardar archivos dentro de él.
 * 
 * Rutas disponibles:
 * - GET /fotos
 * - GET /fotos/{id}
 * - GET /fotos/reporte/{id}
 * - POST /fotos
 * - DELETE /fotos/{id}
 * 
 * El archivo se guarda en /fotos_reportes y se registra su ruta relativa en la base de datos.
 * 
 * @author Yariangel Aray
 */
@Path("/fotos")
public class FotoController {

    private final FotoDAO dao = new FotoDAO();
    private final ReporteDAO reporteDao = new ReporteDAO();
    private static final String CARPETA_FOTOS = "/fotos_reportes"; // Carpeta donde se guardan las fotos físicamente

    /**
     * Retorna todas las fotos registradas.
     *
     * @return Lista de fotos o error si no hay.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodas() {
        try {
            List<Foto> fotos = dao.getAll();
            if (fotos.isEmpty()) return ResponseProvider.error("No se encontraron fotos", 404);
            return ResponseProvider.success(fotos, "Fotos encontradas", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca una foto por su ID único.
     *
     * @param id ID de la foto a buscar
     * @return Foto encontrada o error si no existe
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Foto foto = dao.getById(id);
            if (foto == null) return ResponseProvider.error("Foto no encontrada", 404);
            return ResponseProvider.success(foto, "Foto encontrada", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    /**
     * Obtiene todas las fotos relacionadas con un reporte por ID de reporte.
     *
     * @param id ID del reporte
     * @return Lista de fotos asociadas
     */
    @GET
    @Path("/reporte/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerFotosPorReporteId(@PathParam("id") int id) {
        try {
            List<Foto> fotos = dao.getAllByIdReporte(id);
            if (fotos.isEmpty()) return ResponseProvider.success(null, "No se encontraron fotos para este reporte", 204);
            return ResponseProvider.success(fotos, "Fotos obtenidas correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Guarda una imagen en el servidor y en la base de datos asociada a un reporte.
     *
     * @param archivoStream Stream del archivo recibido
     * @param fileDetail Detalles del archivo (nombre original)
     * @param reporteId ID del reporte asociado
     * @param context Contexto del servlet para obtener ruta física
     * @return Foto registrada o error si falla
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subirFoto(
            @FormDataParam("foto") InputStream archivoStream,
            @FormDataParam("foto") FormDataContentDisposition fileDetail,
            @FormDataParam("reporte_id") int reporteId,
            @Context ServletContext context) {
        try {
            // Validación: el reporte debe existir
            if (reporteDao.getById(reporteId) == null) {
                return ResponseProvider.error("El reporte asociado a esta foto no existe.", 404);
            }
            
            // Ruta física absoluta donde se guardará la imagen
            String basePath = context.getRealPath(CARPETA_FOTOS);

            // Crea la carpeta si no existe
            Files.createDirectories(Paths.get(basePath));

            // Obtiene la extensión original del archivo (ej: .jpg)
            String extension = fileDetail.getFileName().contains(".") ? fileDetail.getFileName().substring(fileDetail.getFileName().lastIndexOf(".")) : "";

            // Genera un nombre único para evitar colisiones (foto_reporte_5_20250805134250123.jpg)
            String nombreUnico = "foto_reporte_" + reporteId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + extension;

            // Define la ruta completa del archivo destino
            // Se usa java.nio.file.Path explícitamente para evitar conflicto con javax.ws.rs.Path (anotación usada en JAX-RS).
            java.nio.file.Path rutaDestino = Paths.get(basePath, nombreUnico);

            // Copia el archivo recibido a la ruta destino
            Files.copy(archivoStream, rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Crea el objeto Foto con la URL relativa al servidor
            Foto foto = new Foto();
            foto.setUrl(CARPETA_FOTOS + "/" + nombreUnico); // Ej: /fotos_reportes/foto_reporte_5_20250805134250123.jpg
            foto.setReporte_id(reporteId);

            // Guarda en la base de datos y retorna la respuesta
            Foto creada = dao.create(foto);
            return ResponseProvider.success(creada, "Foto subida correctamente", 201);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseProvider.error("Error al guardar la imagen", 500);
        }
    }

    /**
     * Elimina una foto por ID, tanto el archivo físico como su registro en la base de datos.
     *
     * @param id ID de la foto a eliminar
     * @return Respuesta indicando éxito o fallo
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarFoto(@PathParam("id") int id) {
        try {
            // Buscar la foto por ID
            Foto foto = dao.getById(id);
            if (foto == null) return ResponseProvider.error("Foto no encontrada", 404);

            // Intentar borrar el archivo físico
            try {
                // Se usa java.nio.file.Path explícitamente para evitar conflicto con javax.ws.rs.Path (anotación usada en JAX-RS).
                java.nio.file.Path ruta = Paths.get("." + foto.getUrl()); // Ruta relativa convertida a absoluta
                Files.deleteIfExists(ruta); // Borra si existe
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseProvider.error("No se pudo eliminar el archivo físico", 500);
            }

            // Elimina el registro en la base de datos
            boolean eliminada = dao.delete(id);
            if (!eliminada) return ResponseProvider.error("Error al eliminar de la base de datos", 500);

            return ResponseProvider.success(null, "Foto eliminada correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
