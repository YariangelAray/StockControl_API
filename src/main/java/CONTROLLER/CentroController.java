package controller;

import model.dao.CentroDAO;
import model.dao.AmbienteDAO;
import model.Centro;
import model.Ambiente;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los centros.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar centros.
 *
 * Rutas disponibles:
 * - GET /centros: Listar todos los centros.
 * - GET /centros/{id}: Buscar centro por ID.
 * - POST /centros: Crear nuevo centro.
 * - PUT /centros/{id}: Actualizar centro existente.
 * - DELETE /centros/{id}: Eliminar centro.
 * 
 * @author Yariangel Aray
 */
@Path("/centros")
public class CentroController {

    private final CentroDAO dao;           // Acceso a datos de centros
    private final AmbienteDAO ambienteDao; // Acceso a datos de ambientes relacionados

    public CentroController() {
        // Inicializa los DAOs
        dao = new CentroDAO();
        ambienteDao = new AmbienteDAO();
    }

    /**
     * Obtiene todos los centros registrados en el sistema.
     *
     * @return Lista de centros o error si no se encuentran resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Consulta todos los centros desde la base de datos
            List<Centro> centros = dao.getAll();

            // Verifica si la lista está vacía
            if (centros.isEmpty()) {
                return ResponseProvider.error("No se encontraron centros", 404);
            }

            // Retorna la lista si hay resultados
            return ResponseProvider.success(centros, "Centros obtenidos correctamente", 200);

        } catch (Exception e) {
            // Si ocurre una excepción, la imprime y retorna error 500
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un centro por su identificador único.
     *
     * @param id ID del centro a consultar.
     * @return Centro encontrado o mensaje de error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCentro(@PathParam("id") int id) {
        try {
            // Consulta el centro por ID
            Centro centro = dao.getById(id);

            // Si no existe, retorna mensaje de no encontrado
            if (centro == null) {
                return ResponseProvider.error("Centro no encontrado", 404);
            }

            // Retorna el centro si fue encontrado
            return ResponseProvider.success(centro, "Centro obtenido correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo centro en el sistema.
     * Requiere validación de campos.
     *
     * @param centro Objeto JSON con los datos del centro.
     * @return Centro creado o mensaje de error si falla.
     */
    @POST
    @ValidarCampos(entidad = "centro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearCentro(Centro centro) {
        try {
            // Intenta registrar el nuevo centro
            Centro nuevoCentro = dao.create(centro);

            // Verifica si se creó correctamente
            if (nuevoCentro != null) {
                return ResponseProvider.success(nuevoCentro, "Centro creado correctamente", 201);
            } else {
                return ResponseProvider.error("Error al crear el centro", 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un centro existente con nuevos datos.
     * Valida que el centro exista previamente.
     *
     * @param id     ID del centro a actualizar.
     * @param centro Nuevos datos para el centro.
     * @return Centro actualizado o error si no existe.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "centro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCentro(@PathParam("id") int id, Centro centro) {
        try {
            // Verifica que el centro exista
            Centro centroExistente = dao.getById(id);
            if (centroExistente == null) {
                return ResponseProvider.error("Centro no encontrado", 404);
            }

            // Intenta actualizar el centro
            Centro centroActualizado = dao.update(id, centro);

            // Si se actualizó correctamente, se retorna
            if (centroActualizado != null) {
                return ResponseProvider.success(centroActualizado, "Centro actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el centro", 404);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un centro del sistema, solo si no tiene ambientes asociados.
     *
     * @param id ID del centro a eliminar.
     * @return Respuesta de éxito o error si hay ambientes relacionados o no existe.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCentro(@PathParam("id") int id) {
        try {
            // Verifica que el centro exista
            Centro centroExistente = dao.getById(id);
            if (centroExistente == null) {
                return ResponseProvider.error("Centro no encontrado", 404);
            }

            // Consulta los ambientes que dependen del centro
            List<Ambiente> ambientes = ambienteDao.getAllByCentroId(id);

            // Si tiene ambientes asociados, no se puede eliminar
            if (ambientes != null && !ambientes.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el centro porque tiene ambientes asociados", 409);
            }

            // Intenta eliminar el centro
            boolean eliminado = dao.delete(id);

            // Verifica resultado de la eliminación
            if (eliminado) {
                return ResponseProvider.success(null, "Centro eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el centro", 500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
