package service;

import model.entity.Ciudad;
import model.dao.CiudadDAO;
import providers.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.CentroDAO;
import model.entity.Centro;

/**
 * Servicio que maneja la lógica de negocio relacionada con ciudades.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodas()
 * - obtenerCiudad(int id)
 * - crearCiudad(Ciudad ciudad)
 * - actualizarCiudad(int id, Ciudad ciudad)
 * - eliminarCiudad(int id)
 * 
 * @author Yariangel Aray
 */
public class CiudadService {

    private CiudadDAO dao; // Instancia del DAO para acceder a los datos de ciudades

    public CiudadService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new CiudadDAO();
    }

    /**
     * Retorna todas las ciudades registradas en la base de datos.
     *
     * @return Lista de ciudades o error si no hay resultados.
     */
    public Response obtenerTodas() {
        // Obtiene la lista de ciudades desde el DAO
        List<Ciudad> ciudades = dao.getAll();

        // Verifica si la lista está vacía
        if (ciudades.isEmpty()) {
            // Retorna un error si no se encontraron ciudades
            return ResponseProvider.error("No se encontraron ciudades", 404);
        }

        // Retorna la lista de ciudades si se encontraron
        return ResponseProvider.success(ciudades, "Ciudades obtenidas correctamente", 200);
    }

    /**
     * Busca y retorna una ciudad por su ID.
     *
     * @param id ID de la ciudad.
     * @return Ciudad encontrada o error si no existe.
     */
    public Response obtenerCiudad(int id) {
        // Busca la ciudad por ID en el DAO
        Ciudad ciudad = dao.getById(id);

        // Verifica si la ciudad fue encontrada
        if (ciudad == null) {
            // Retorna un error si no se encontró la ciudad
            return ResponseProvider.error("Ciudad no encontrada", 404);
        }

        // Retorna la ciudad si fue encontrada
        return ResponseProvider.success(ciudad, "Ciudad obtenida correctamente", 200);
    }

    /**
     * Crea una nueva ciudad.
     *
     * @param ciudad Objeto con los datos de la nueva ciudad.
     * @return Ciudad creada o mensaje de error si falla el registro.
     */
    public Response crearCiudad(Ciudad ciudad) {
        // Intentar crear la ciudad en la base de datos
        Ciudad nuevaCiudad = dao.create(ciudad);
        if (nuevaCiudad != null) {
            // Retorna la nueva ciudad si fue creada correctamente
            return ResponseProvider.success(nuevaCiudad, "Ciudad creada correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear la ciudad
            return ResponseProvider.error("Error al crear la ciudad", 400);
        }
    }

    /**
     * Actualiza los datos de una ciudad existente.
     *
     * @param id ID de la ciudad a actualizar.
     * @param ciudad Objeto con los nuevos datos.
     * @return Ciudad actualizada o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarCiudad(int id, Ciudad ciudad) {
        // Validar que la ciudad exista
        Ciudad ciudadExistente = dao.getById(id);
        if (ciudadExistente == null) {
            // Retorna un error si la ciudad no fue encontrada
            return ResponseProvider.error("Ciudad no encontrada", 404);
        }

        // Intentar actualizar la ciudad en la base de datos
        Ciudad ciudadActualizada = dao.update(id, ciudad);
        if (ciudadActualizada != null) {
            // Retorna la ciudad actualizada si fue exitoso
            return ResponseProvider.success(ciudadActualizada, "Ciudad actualizada correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar la ciudad
            return ResponseProvider.error("Error al actualizar la ciudad", 404);
        }
    }

    /**
     * Elimina una ciudad existente por su ID.
     *
     * @param id ID de la ciudad a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarCiudad(int id) {
        // Verificar existencia de la ciudad
        Ciudad ciudadExistente = dao.getById(id);
        if (ciudadExistente == null) {
            // Retorna un error si la ciudad no fue encontrada
            return ResponseProvider.error("Ciudad no encontrada", 404);
        }
        
        // Se crea una instancia de CentroDAO para acceder a los centros
        CentroDAO centroDao = new CentroDAO();

        // Se obtiene la lista de centros que están asociados a la ciudad con el ID proporcionado
        List<Centro> centros = centroDao.getAllByCiudadId(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay centros en esa ciudad)
        if (centros != null && !centros.isEmpty())
            // Si hay centros asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar
            return ResponseProvider.error("No se puede eliminar la ciudad porque tiene centros asociados", 409);

        // Intentar eliminar la ciudad de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si la ciudad fue eliminada
            return ResponseProvider.success(null, "Ciudad eliminada correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar la ciudad
            return ResponseProvider.error("Error al eliminar la ciudad", 500);
        }
    }
}
