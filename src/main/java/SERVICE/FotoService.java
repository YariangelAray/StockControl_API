package service;

import model.dao.FotoDAO;
import model.entity.Foto;
import providers.ResponseProvider;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para manejar la lógica relacionada con las fotos de los reportes.
 * - Guarda físicamente el archivo en una carpeta
 * - Guarda la URL en la base de datos
 * - Permite listar y eliminar fotos
 *
 * El uso del ServletContext permite guardar archivos en la ruta real del proyecto desplegado,
 * sin depender de rutas fijas del sistema operativo o IDE.
 *
 * @author Yariangel Aray
 */
public class FotoService {

    // DAO que maneja las operaciones con la tabla 'fotos'
    private FotoDAO dao = new FotoDAO();

    // Carpeta dentro del proyecto donde se guardarán las fotos
    private static final String CARPETA_FOTOS = "/fotos_reportes";

    /**
     * Sube una foto al servidor y guarda la ruta en la base de datos.
     *
     * @param archivoStream InputStream del archivo recibido
     * @param nombreArchivo Nombre original del archivo (no se usará directamente)
     * @param reporteId ID del reporte al que pertenece
     * @param context ServletContext que permite obtener la ruta real del proyecto desplegado
     * @return Respuesta con el objeto Foto creado o error
     */
    public Response subirFoto(InputStream archivoStream, String nombreArchivo, int reporteId, ServletContext context) {
        try {
            // Usamos ServletContext para obtener la ruta física real dentro del WAR desplegado
            // Esto garantiza que la imagen se guarde en una carpeta accesible públicamente sin importar el entorno
            String basePath = context.getRealPath(CARPETA_FOTOS);

            // Asegura que la carpeta exista
            Files.createDirectories(Paths.get(basePath));

            // Genera un nombre único para evitar conflictos (ej: foto_reporte_5_20250629104523001.jpg)
            String extension = nombreArchivo.contains(".") ? nombreArchivo.substring(nombreArchivo.lastIndexOf(".")) : "";
            String nombreUnico = "foto_reporte_" + reporteId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + extension;

            // Ruta completa donde se guardará el archivo
            Path rutaDestino = Paths.get(basePath, nombreUnico);

            System.out.println("Ruta en donde se guardará la imagen: " + rutaDestino.toString());
            
            // Copia el archivo desde el stream a la carpeta
            Files.copy(archivoStream, rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Crea el objeto Foto con la URL relativa para que pueda ser accedida desde el navegador
            Foto foto = new Foto();
            foto.setUrl(CARPETA_FOTOS + "/" + nombreUnico); // URL relativa (accesible vía navegador)
            foto.setReporte_id(reporteId);

            // Guarda la foto en la base de datos
            Foto creada = dao.create(foto);
            return ResponseProvider.success(creada, "Foto subida correctamente", 201);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseProvider.error("Error al guardar la imagen", 500);
        }
    }

    /**
     * Retorna todas las fotos
     */
    public Response obtenerTodas() {
        List<Foto> fotos = dao.getAll();
        if (fotos.isEmpty()) return ResponseProvider.error("No se encontraron fotos", 404);
        return ResponseProvider.success(fotos, "Fotos encontradas", 200);
    }

    /**
     * Retorna una foto por su ID
     */
    public Response obtenerPorId(int id) {
        Foto foto = dao.getById(id);
        if (foto == null) return ResponseProvider.error("Foto no encontrada", 404);
        return ResponseProvider.success(foto, "Foto encontrada", 200);
    }
    
    /**
     * Obtiene todas las fotos asociadas a un reporte por su ID.
     *
     * @param reporteId ID del reporte al que pertenecen las fotos.
     * @return Lista de fotos o error si no se encontraron.
     */
    public Response obtenerFotosPorReporte(int reporteId) {
        List<Foto> fotos = dao.getAllByIdReporte(reporteId);

        if (fotos.isEmpty()) {
            return ResponseProvider.error("No se encontraron fotos para este reporte", 404);
        }

        return ResponseProvider.success(fotos, "Fotos obtenidas correctamente", 200);
    }

    /**
     * Lista todas las fotos asociadas a un reporte
     */
    public Response obtenerPorReporteId(int reporteId) {
        List<Foto> fotos = dao.getAllByIdReporte(reporteId);
        if (fotos.isEmpty()) return ResponseProvider.error("No se encontraron fotos para el reporte", 404);
        return ResponseProvider.success(fotos, "Fotos del reporte obtenidas", 200);
    }

    /**
     * Elimina una foto del sistema (archivo físico y base de datos)
     */
    public Response eliminarFoto(int id) {
        Foto foto = dao.getById(id);
        if (foto == null) return ResponseProvider.error("Foto no encontrada", 404);

        // Intenta eliminar el archivo físico
        try {
            // Obtenemos la ruta relativa como absoluta con Paths.get
            Path ruta = Paths.get("." + foto.getUrl());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseProvider.error("No se pudo eliminar el archivo físico", 500);
        }

        // Elimina el registro en la base de datos
        boolean eliminada = dao.delete(id);
        if (!eliminada) return ResponseProvider.error("Error al eliminar de la base de datos", 500);

        return ResponseProvider.success(null, "Foto eliminada correctamente", 200);
    }
}
