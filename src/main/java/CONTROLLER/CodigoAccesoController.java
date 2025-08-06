package controller;

import model.CodigoAcceso;
import model.AccesoTemporal;
import model.Inventario;
import model.dao.CodigoAccesoDAO;
import model.dao.AccesoTemporalDAO;
import model.dao.InventarioDAO;
import model.dto.HorasDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que gestiona los códigos de acceso temporales para inventarios.
 * 
 * Esta clase expone endpoints HTTP que permiten:
 * - Generar códigos únicos que otorgan acceso temporal a un inventario.
 * - Validar si un código está activo y es válido.
 * - Consultar el código activo asociado a un inventario.
 * - Eliminar todos los accesos temporales y el código asociado cuando sea necesario.
 * 
 * Los códigos generados tienen una duración limitada en horas y minutos, y son
 * utilizados principalmente por usuarios con permisos restringidos para consultar
 * o editar inventarios específicos sin necesidad de un acceso permanente.
 * 
 * Endpoints disponibles:
 * - POST /codigos-acceso/generar/{inventarioId}: Genera un nuevo código con expiración
 * - GET /codigos-acceso/validar/{codigo}: Valida si un código está activo
 * - GET /codigos-acceso/inventario/{id}: Obtiene el código activo de un inventario
 * - DELETE /codigos-acceso/inventario/{inventarioId}: Elimina accesos y código activo
 * 
 * @author Yariangel Aray
 */
@Path("/codigos-acceso") // Define la ruta base para todos los endpoints de este controlador
public class CodigoAccesoController {

    // DAO para operaciones de base de datos con códigos de acceso
    private final CodigoAccesoDAO dao;
    // DAO para operaciones de base de datos con inventarios
    private final InventarioDAO daoInventario;
    // DAO para operaciones de base de datos con accesos temporales
    private final AccesoTemporalDAO daoAcceso;

    /**
     * Constructor que inicializa los DAOs requeridos.
     */
    public CodigoAccesoController() {
        // Inicializa el DAO de códigos de acceso
        this.dao = new CodigoAccesoDAO();
        // Inicializa el DAO de inventarios
        this.daoInventario = new InventarioDAO();
        // Inicializa el DAO de accesos temporales
        this.daoAcceso = new AccesoTemporalDAO();
    }

    /**
     * Genera un nuevo código de acceso para un inventario válido por cierto tiempo (en horas y minutos).
     * 
     * @param inventarioId ID del inventario al que se asociará el código.
     * @param horas DTO que contiene las horas y minutos de duración del código.
     * @return Código generado o mensaje de error si ya existe uno activo o falla la creación.
     */
    @POST // Método HTTP POST
    @Path("/generar/{inventarioId}") // Ruta específica con parámetro inventarioId
    @Consumes(MediaType.APPLICATION_JSON) // Acepta datos en formato JSON
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response generarCodigo(@PathParam("inventarioId") int inventarioId, HorasDTO horas) {
        try {
            // Validar que el inventario exista
            Inventario inventario = daoInventario.getById(inventarioId);
            // Si el inventario no existe, retorna error 404
            if (inventario == null) {
                return ResponseProvider.error("El inventario especificado no existe", 404);
            }
            
            // Validar si ya hay un código activo para este inventario
            CodigoAcceso codigoExistente = dao.getCodigoActivo(inventarioId);
            // Si ya existe un código activo, retorna error de conflicto 409
            if (codigoExistente != null) {
                return ResponseProvider.error("Ya existe un código activo para este inventario. Espere a que expire antes de generar uno nuevo.", 409);
            }

            // Generar un código aleatorio simple de 8 caracteres
            String codigoGenerado = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Calcular fecha de expiración usando los valores del DTO
            Timestamp expiracion = Timestamp.from(Instant.now().plusSeconds(
                    horas.getHoras() * 3600L + horas.getMinutos() * 60L // Convierte horas a segundos + minutos a segundos
            ));

            // Construir el objeto
            CodigoAcceso codigo = new CodigoAcceso();
            // Asigna el código generado
            codigo.setCodigo(codigoGenerado);
            // Asigna el ID del inventario
            codigo.setInventario_id(inventarioId);
            // Asigna la fecha de expiración calculada
            codigo.setFecha_expiracion(expiracion);
            
            if (dao.create(codigo)) {
                // Si se crea exitosamente, retorna el código con status 201
                return ResponseProvider.success(codigo, "Código generado exitosamente", 201);
            }

            // Si falla la creación, retorna error 500
            return ResponseProvider.error("No se pudo generar el código", 500);

        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Valida si un código de acceso está activo y es válido.
     * 
     * @param codigo Código alfanumérico a validar.
     * @return Información del código si es válido o error si no existe o expiró.
     */
    @GET // Método HTTP GET
    @Path("/validar/{codigo}") // Ruta específica con parámetro codigo
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response validarCodigo(@PathParam("codigo") String codigo) {
        try {
            // Busca el código en la base de datos y verifica si está válido
            CodigoAcceso encontrado = dao.searchValid(codigo);

            // Si el código existe y está válido
            if (encontrado != null) {
                // Adjunta el nombre del inventario
                Inventario inventario = daoInventario.getById(encontrado.getInventario_id());
                // Establece el nombre del inventario en el objeto código
                encontrado.setNombre_inventario(inventario.getNombre());
                // Retorna el código válido con status 200
                return ResponseProvider.success(encontrado, "Código válido", 200);
            }

            // Si el código no existe o expiró, retorna error 404
            return ResponseProvider.error("Código inválido o expirado", 404);
        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene el código activo de un inventario, si existe alguno.
     * 
     * @param id ID del inventario.
     * @return Código activo o estado 204 si no existe.
     */
    @GET // Método HTTP GET
    @Path("/inventario/{id}") // Ruta específica con parámetro id
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response obtenerCodigoActivo(@PathParam("id") int id) {
        try {
            // Validar que el inventario exista
            Inventario inventario = daoInventario.getById(id);
            // Si el inventario no existe, retorna error 404
            if (inventario == null) {
                return ResponseProvider.error("El inventario especificado no existe", 404);
            }
            
            // Busca el código activo para el inventario específico
            CodigoAcceso codigo = dao.getCodigoActivo(id);
            // Si no existe código activo, retorna error 204 (no content)
            if (codigo == null)
                return ResponseProvider.error("No hay código activo", 204);

            // Retorna el código activo encontrado con status 200
            return ResponseProvider.success(codigo, "Código activo del inventario", 200);
        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina todos los accesos temporales y el código activo asociado a un inventario.
     * 
     * @param inventarioId ID del inventario cuyos accesos y código se eliminarán.
     * @return Mensaje de éxito si se eliminó, o mensaje si no había nada que eliminar.
     */
    @DELETE // Método HTTP DELETE
    @Path("/inventario/{inventarioId}") // Ruta específica con parámetro inventarioId
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response eliminarAccesos(@PathParam("inventarioId") int inventarioId) {
        try {            
            // Validar que el inventario exista
            Inventario inventario = daoInventario.getById(inventarioId);
            // Si el inventario no existe, retorna error 404
            if (inventario == null) {
                return ResponseProvider.error("El inventario especificado no existe", 404);
            }
            
            // Busca el código asociado al inventario
            CodigoAcceso codigo = dao.getByIdInventario(inventarioId);
            // Si no hay código, retorna mensaje informativo con status 204
            if (codigo == null) {
                return ResponseProvider.success(null, "No hay código activo para este inventario", 204);
            }

            // Obtiene la lista de accesos temporales asociados al código
            List<AccesoTemporal> accesos = daoAcceso.getAccesosPorCodigo(inventarioId);
            // Si no hay accesos para eliminar, retorna mensaje informativo con status 204
            if (accesos.isEmpty()) {
                return ResponseProvider.success(null, "No hay usuarios con acceso para eliminar", 204);
            }

            // Intenta eliminar todos los accesos temporales asociados al código
            boolean eliminadoAccesos = daoAcceso.deleteAccesosPorCodigo(codigo.getId());
            // Si se eliminaron los accesos exitosamente
            if (eliminadoAccesos) {
                // Intenta eliminar el código de acceso
                boolean eliminadoCodigo = dao.deleteCodigoPorInventario(inventarioId);
                // Si se eliminó el código exitosamente
                if (eliminadoCodigo) {
                    // Retorna mensaje de éxito con status 200
                    return ResponseProvider.success(null, "Accesos del inventario eliminados correctamente", 200);
                }
                // Si falló la eliminación del código, retorna error 500
                return ResponseProvider.error("Error al eliminar el código de acceso", 500);
            }

            // Si falló la eliminación de accesos, retorna error 500
            return ResponseProvider.error("Error al eliminar los accesos del inventario", 500);

        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno al eliminar accesos", 500);
        }
    }
}