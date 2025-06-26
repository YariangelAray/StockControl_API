package service;

import model.entity.Centro;
import model.dao.CentroDAO;
import model.dao.AmbienteDAO;
import model.entity.Ambiente;
import providers.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Servicio que maneja la lógica de negocio relacionada con centros.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerCentro(int id)
 * - crearCentro(Centro centro)
 * - actualizarCentro(int id, Centro centro)
 * - eliminarCentro(int id)
 * 
 * @author Yariangel Aray
 */
public class CentroService {

    private CentroDAO dao; // Instancia del DAO para acceder a los datos de centros

    public CentroService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new CentroDAO();
    }

    /**
     * Retorna todos los centros registrados en la base de datos.
     *
     * @return Lista de centros o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de centros desde el DAO
        List<Centro> centros = dao.getAll();

        // Verifica si la lista está vacía
        if (centros.isEmpty()) {
            // Retorna un error si no se encontraron centros
            return ResponseProvider.error("No se encontraron centros", 404);
        }

        // Retorna la lista de centros si se encontraron
        return ResponseProvider.success(centros, "Centros obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un centro por su ID.
     *
     * @param id ID del centro.
     * @return Centro encontrado o error si no existe.
     */
    public Response obtenerCentro(int id) {
        // Busca el centro por ID en el DAO
        Centro centro = dao.getById(id);

        // Verifica si el centro fue encontrado
        if (centro == null) {
            // Retorna un error si no se encontró el centro
            return ResponseProvider.error("Centro no encontrado", 404);
        }

        // Retorna el centro si fue encontrado
        return ResponseProvider.success(centro, "Centro obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo centro.
     *
     * @param centro Objeto con los datos del nuevo centro.
     * @return Centro creado o mensaje de error si falla el registro.
     */
    public Response crearCentro(Centro centro) {
        // Intentar crear el centro en la base de datos
        Centro nuevoCentro = dao.create(centro);
        if (nuevoCentro != null) {
            // Retorna el nuevo centro si fue creado correctamente
            return ResponseProvider.success(nuevoCentro, "Centro creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el centro
            return ResponseProvider.error("Error al crear el centro", 400);
        }
    }

    /**
     * Actualiza los datos de un centro existente.
     *
     * @param id ID del centro a actualizar.
     * @param centro Objeto con los nuevos datos.
     * @return Centro actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarCentro(int id, Centro centro) {
        // Validar que el centro exista
        Centro centroExistente = dao.getById(id);
        if (centroExistente == null) {
            // Retorna un error si el centro no fue encontrado
            return ResponseProvider.error("Centro no encontrado", 404);
        }

        // Intentar actualizar el centro en la base de datos
        Centro centroActualizado = dao.update(id, centro);
        if (centroActualizado != null) {
            // Retorna el centro actualizado si fue exitoso
            return ResponseProvider.success(centroActualizado, "Centro actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el centro
            return ResponseProvider.error("Error al actualizar el centro", 404);
        }
    }

    /**
     * Elimina un centro existente por su ID.
     *
     * @param id ID del centro a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarCentro(int id) {
        // Verificar existencia del centro
        Centro centroExistente = dao.getById(id);
        if (centroExistente == null) {
            // Retorna un error si el centro no fue encontrado
            return ResponseProvider.error("Centro no encontrado", 404);
        }

        // Se crea una instancia de AmbienteDAO para acceder a los ambientes
        AmbienteDAO ambienteDao = new AmbienteDAO();

        // Se obtiene la lista de ambientes que pertenecen al centro con el ID proporcionado
        List<Ambiente> ambientes = ambienteDao.getAllByCentroId(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay ambientes asociados a ese centro)
        if (ambientes != null && !ambientes.isEmpty())
            // Si hay ambientes asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar el centro
            return ResponseProvider.error("No se puede eliminar el centro porque tiene ambientes asociados", 409);

        // Intentar eliminar el centro de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el centro fue eliminado
            return ResponseProvider.success(null, "Centro eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el centro
            return ResponseProvider.error("Error al eliminar el centro", 500);
        }
    }
}
