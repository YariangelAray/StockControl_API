// FotoController.java
package controller;

import service.FotoService;
import providers.ResponseProvider;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 * Controlador REST para gestionar operaciones relacionadas con las fotos.
 * 
 * Rutas disponibles:
 * - GET /fotos: Listar todas las fotos.
 * - GET /fotos/{id}: Buscar foto por ID.
 * - POST /fotos: Crear nueva foto.
 * - DELETE /fotos/{id}: Eliminar una foto por ID.
 * 
 * Usa @FormDataParam para recibir archivos y datos tipo multipart/form-data.
 * Usa ServletContext para que el servicio conozca la ruta física del proyecto desplegado.
 *
 * @author Yariangel Aray
 */
@Path("/fotos")
public class FotoController {

    private FotoService service = new FotoService();

    /**
     * Obtiene todas las fotos registradas (útil para depuración o administración).
     *
     * @return Lista de fotos o error si no hay resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodas() {
        try {
            return service.obtenerTodas();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene una foto por su ID único.
     *
     * @param id ID de la foto.
     * @return Objeto Foto o error si no se encuentra.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            return service.obtenerPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Sube una foto asociada a un reporte.
     *
     * Este endpoint recibe una imagen mediante un formulario multipart y la guarda físicamente en
     * una carpeta pública del proyecto. También registra la ruta en la base de datos.
     *
     * @param archivoStream Contenido binario de la imagen
     * @param fileDetail Detalles del archivo (nombre original, tipo, etc.)
     * @param reporteId ID del reporte al que se asociará la imagen
     * @param context Inyectado automáticamente, permite acceder a rutas del proyecto desplegado
     * @return Foto registrada o error
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subirFoto(
            @FormDataParam("foto") InputStream archivoStream,
            @FormDataParam("foto") FormDataContentDisposition fileDetail,
            @FormDataParam("reporte_id") int reporteId,
            @Context ServletContext context
    ) {
        try {
            return service.subirFoto(archivoStream, fileDetail.getFileName(), reporteId, context);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error al subir la imagen", 500);
        }
    }

    /**
     * Elimina una foto por su ID. Borra tanto el archivo físico como el registro en la base de datos.
     *
     * @param id ID de la foto a eliminar
     * @return Mensaje de confirmación o error
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarFoto(@PathParam("id") int id) {
        try {
            return service.eliminarFoto(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
