package service;

import model.entity.Reporte;
import model.dao.ReporteDAO;
import providers.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.FotoDAO;
import model.entity.Foto;

/**
 * Servicio que maneja la lógica de negocio relacionada con reportes.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerReporte(int id)
 * - crearReporte(Reporte reporte)
 * - actualizarReporte(int id, Reporte reporte)
 * - eliminarReporte(int id)
 * 
 * @author Yariangel Aray
 */
public class ReporteService {

    private ReporteDAO dao; // Instancia del DAO para acceder a los datos de reportes

    public ReporteService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new ReporteDAO();
    }

    /**
     * Retorna todos los reportes registrados en la base de datos.
     *
     * @return Lista de reportes o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de reportes desde el DAO
        List<Reporte> reportes = dao.getAll();

        // Verifica si la lista está vacía
        if (reportes.isEmpty()) {
            // Retorna un error si no se encontraron reportes
            return ResponseProvider.error("No se encontraron reportes", 404);
        }

        // Retorna la lista de reportes si se encontraron
        return ResponseProvider.success(reportes, "Reportes obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un reporte por su ID.
     *
     * @param id ID del reporte.
     * @return Reporte encontrado o error si no existe.
     */
    public Response obtenerReporte(int id) {
        // Busca el reporte por ID en el DAO
        Reporte reporte = dao.getById(id);

        // Verifica si el reporte fue encontrado
        if (reporte == null) {
            // Retorna un error si no se encontró el reporte
            return ResponseProvider.error("Reporte no encontrado", 404);
        }

        // Retorna el reporte si fue encontrado
        return ResponseProvider.success(reporte, "Reporte obtenido correctamente", 200);
    }
    
    /**
     * Obtiene todos los reportes asociados a un inventario específico.     
     *
     * @param idInventario ID del inventario del cual se desean obtener los reportes.
     * @return Lista de reportes relacionados o mensaje de error si no se encuentran.
     */
    public Response obtenerReportesPorInventario(int idInventario) {
        // Utiliza el DAO para buscar los reportes filtrados por inventario
        List<Reporte> reportes = dao.getAllByIdInventario(idInventario);

        // Verifica si la lista está vacía
        if (reportes == null || reportes.isEmpty()) {
            // Retorna un error si no se encontraron reportes
            return ResponseProvider.error("No se encontraron reportes para este inventario", 204);
        }

        // Retorna la lista de reportes si existen
        return ResponseProvider.success(reportes, "Reportes obtenidos correctamente", 200);
    }


    /**
     * Crea un nuevo reporte.
     *
     * @param reporte Objeto con los datos del nuevo reporte.
     * @return Reporte creado o mensaje de error si falla el registro.
     */
    public Response crearReporte(Reporte reporte) {
        // Intentar crear el reporte en la base de datos
        Reporte nuevoReporte = dao.create(reporte);
        if (nuevoReporte != null) {
            // Retorna el nuevo reporte si fue creado correctamente
            return ResponseProvider.success(nuevoReporte, "Reporte creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el reporte
            return ResponseProvider.error("Error al crear el reporte", 400);
        }
    }

    /**
     * Actualiza los datos de un reporte existente.
     *
     * @param id ID del reporte a actualizar.
     * @param reporte Objeto con los nuevos datos.
     * @return Reporte actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarReporte(int id, Reporte reporte) {
        // Validar que el reporte exista
        Reporte reporteExistente = dao.getById(id);
        if (reporteExistente == null) {
            // Retorna un error si el reporte no fue encontrado
            return ResponseProvider.error("Reporte no encontrado", 404);
        }

        // Intentar actualizar el reporte en la base de datos
        Reporte reporteActualizado = dao.update(id, reporte);
        if (reporteActualizado != null) {
            // Retorna el reporte actualizado si fue exitoso
            return ResponseProvider.success(reporteActualizado, "Reporte actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el reporte
            return ResponseProvider.error("Error al actualizar el reporte", 404);
        }
    }

    /**
     * Elimina un reporte existente por su ID.
     *
     * @param id ID del reporte a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarReporte(int id) {
        // Verificar existencia del reporte
        Reporte reporteExistente = dao.getById(id);
        if (reporteExistente == null) {
            // Retorna un error si el reporte no fue encontrado
            return ResponseProvider.error("Reporte no encontrado", 404);
        }

        // Se crea una instancia de FotoDAO para acceder a las fotos
        FotoDAO fotoDao = new FotoDAO();

        // Se obtiene la lista de fotos que estén asociadas al reporte
        List<Foto> fotos = fotoDao.getAllByIdReporte(id);

        // Se verifica si hay fotos asociadas al reporte
        if (fotos != null && !fotos.isEmpty()) {
            // Si hay fotos, se retorna un error 409 indicando conflicto
            return ResponseProvider.error("No se puede eliminar el reporte porque tiene fotos asociadas", 409);
        }

        // Intentar eliminar el reporte de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el reporte fue eliminado
            return ResponseProvider.success(null, "Reporte eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el reporte
            return ResponseProvider.error("Error al eliminar el reporte", 500);
        }
    }
}
