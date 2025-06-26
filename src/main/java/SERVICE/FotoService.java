package service;

import model.dao.FotoDAO;
import model.entity.Foto;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** 
 * Servicio para manejar la lógica relacionada con las fotos de los reportes.
 *
 * 
 *
 * @author Yariangel Aray
 */
public class FotoService {

    // DAO que maneja las operaciones con la tabla 'fotos'
    private FotoDAO dao = new FotoDAO();

    // Carpeta donde se guardarán las fotos físicamente (relativa al proyecto)
    private static final String CARPETA_FOTOS = "fotos_reportes";

    /**
     * Sube una foto al servidor y guarda la ruta en la base de datos.
     *
     * @param archivoStream InputStream del archivo recibido
     * @param nombreArchivo Nombre original del archivo (no se usará directamente)
     * @param reporteId ID del reporte al que pertenece
     * @return Respuesta con el objeto Foto creado o error
     */
    public Response subirFoto(InputStream archivoStream, String nombreArchivo, int reporteId) {
        try {
            // Ruta real donde se deben guardar las imágenes (dentro de webapp)
            // "user.dir" devuelve el directorio donde se ejecuta el proyecto
            String basePath = System.getProperty("user.dir") + "/src/main/webapp/" + CARPETA_FOTOS;
 
            // Asegura que la carpeta exista
            Files.createDirectories(Paths.get(basePath));

            // Genera un nombre único con fecha y reporteId
            String extension = nombreArchivo.contains(".") ? nombreArchivo.substring(nombreArchivo.lastIndexOf(".")) : "";
            String nombreUnico = "foto_" + reporteId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + extension;

            // Ruta completa del archivo
            Path rutaDestino = Paths.get(basePath, nombreUnico);

            // Copia la imagen al destino
            Files.copy(archivoStream, rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Guarda la URL relativa (para accederla desde el navegador)
            Foto foto = new Foto();
            foto.setUrl("/" + CARPETA_FOTOS + "/" + nombreUnico);
            foto.setReporteId(reporteId);

            // Inserta en base de datos
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
            Path ruta = Paths.get("." + foto.getUrl());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseProvider.error("No se pudo eliminar el archivo físico", 500);
        }

        // Elimina el registro en la base de datos
        boolean eliminada = dao.delete(id);
        if (!eliminada) return ResponseProvider.error("Error al eliminar la foto de la base de datos", 500);

        return ResponseProvider.success(null, "Foto eliminada correctamente", 200);
    }
}
