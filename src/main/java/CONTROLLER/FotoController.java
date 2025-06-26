package controller;

import service.FotoService;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 * Controlador REST para gestionar operaciones relacionadas con las fotos de reportes.
 * Permite subir, obtener y eliminar fotos.
 *
 * Rutas disponibles:
 * - GET /fotos: Listar todas las fotos.
 * - GET /fotos/{id}: Obtener una foto por su ID.
 * - GET /fotos/reporte/{id}: Obtener todas las fotos asociadas a un reporte.
 * - POST /fotos: Subir una nueva foto.
 * - DELETE /fotos/{id}: Eliminar una foto.
 * 
 * @author Tú
 */
@Path("/fotos") // Ruta base para el controlador de fotos
public class FotoController {

    // Instancia del servicio encargado de la lógica de negocio para fotos
    private FotoService service = new FotoService();

    /**
     * Obtiene todas las fotos registradas.
     *
     * @return Lista de fotos o error si no hay resultados.
     */
    @GET // Método HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // La respuesta será en formato JSON
    public Response obtenerTodas() {
        try {
            // Llama al servicio para obtener todas las fotos
            return service.obtenerTodas();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            return ResponseProvider.error("Error interno en el servidor", 500); // Respuesta de error 500
        }
    }

    /**
     * Obtiene una foto por su ID.
     *
     * @param id ID de la foto a consultar.
     * @return Foto encontrada o mensaje de error si no existe.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro {id}
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            // Llama al servicio para obtener la foto por ID
            return service.obtenerPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Sube una nueva foto y la asocia a un reporte.
     * 
     * @param archivoStream InputStream del archivo recibido.
     * @param fileDetail Detalles del archivo (incluye nombre original).
     * @param reporteId ID del reporte al que se asociará la foto.
     * @return Foto creada o mensaje de error.
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA) // Recibe datos en formato multipart
    @Produces(MediaType.APPLICATION_JSON)
    public Response subirFoto(
        @FormDataParam("foto") InputStream archivoStream, // El archivo (campo 'foto')
        @FormDataParam("foto") FormDataContentDisposition fileDetail, // Info del archivo
        @FormDataParam("reporte_id") int reporteId // ID del reporte
    ) {
        try {
            // Llama al servicio para subir y registrar la foto
            return service.subirFoto(archivoStream, fileDetail.getFileName(), reporteId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error al subir la imagen", 500);
        }
    }

    /**
     * Elimina una foto por su ID.
     *
     * @param id ID de la foto a eliminar.
     * @return Mensaje de éxito o error si no se puede eliminar.
     */
    @DELETE
    @Path("/{id}") // Ruta para eliminar una foto por ID
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarFoto(@PathParam("id") int id) {
        try {
            // Llama al servicio para eliminar la foto
            return service.eliminarFoto(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
