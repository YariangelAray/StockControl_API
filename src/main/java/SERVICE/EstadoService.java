package service;

import model.dao.EstadoDAO;
import model.dao.ElementoDAO;
import model.Estado;
import model.Elemento;
import utils.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Servicio que maneja la lógica de negocio relacionada con estados.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerEstado(int id)
 * - crearEstado(Estado estado)
 * - actualizarEstado(int id, Estado estado)
 * - eliminarEstado(int id)
 * 
 * @author Yariangel Aray
 */
public class EstadoService {

    private EstadoDAO dao; // Instancia del DAO para acceder a los datos de estados

    public EstadoService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new EstadoDAO();
    }

    /**
     * Retorna todos los estados registrados en la base de datos.
     *
     * @return Lista de estados o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de estados desde el DAO
        List<Estado> estados = dao.getAll();

        // Verifica si la lista está vacía
        if (estados.isEmpty()) {
            // Retorna un error si no se encontraron estados
            return ResponseProvider.error("No se encontraron estados", 404);
        }

        // Retorna la lista de estados si se encontraron
        return ResponseProvider.success(estados, "Estados obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un estado por su ID.
     *
     * @param id ID del estado.
     * @return Estado encontrado o error si no existe.
     */
    public Response obtenerEstado(int id) {
        // Busca el estado por ID en el DAO
        Estado estado = dao.getById(id);

        // Verifica si el estado fue encontrado
        if (estado == null) {
            // Retorna un error si no se encontró el estado
            return ResponseProvider.error("Estado no encontrado", 404);
        }

        // Retorna el estado si fue encontrado
        return ResponseProvider.success(estado, "Estado obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo estado.
     *
     * @param estado Objeto con los datos del nuevo estado.
     * @return Estado creado o mensaje de error si falla el registro.
     */
    public Response crearEstado(Estado estado) {
        // Intentar crear el estado en la base de datos
        Estado nuevoEstado = dao.create(estado);
        if (nuevoEstado != null) {
            // Retorna el nuevo estado si fue creado correctamente
            return ResponseProvider.success(nuevoEstado, "Estado creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el estado
            return ResponseProvider.error("Error al crear el estado", 400);
        }
    }

    /**
     * Actualiza los datos de un estado existente.
     *
     * @param id ID del estado a actualizar.
     * @param estado Objeto con los nuevos datos.
     * @return Estado actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarEstado(int id, Estado estado) {
        // Validar que el estado exista
        Estado estadoExistente = dao.getById(id);
        if (estadoExistente == null) {
            // Retorna un error si el estado no fue encontrado
            return ResponseProvider.error("Estado no encontrado", 404);
        }

        // Intentar actualizar el estado en la base de datos
        Estado estadoActualizado = dao.update(id, estado);
        if (estadoActualizado != null) {
            // Retorna el estado actualizado si fue exitoso
            return ResponseProvider.success(estadoActualizado, "Estado actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el estado
            return ResponseProvider.error("Error al actualizar el estado", 404);
        }
    }

    /**
     * Elimina un estado existente por su ID.
     *
     * @param id ID del estado a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarEstado(int id) {
        // Verificar existencia del estado
        Estado estadoExistente = dao.getById(id);
        if (estadoExistente == null) {
            // Retorna un error si el estado no fue encontrado
            return ResponseProvider.error("Estado no encontrado", 404);
        }

        // Verificar si existen elementos asociados al estado
        ElementoDAO elementoDao = new ElementoDAO();
        List<Elemento> elementosAsociados = elementoDao.getByIdTipoEstado(id);

        // Si hay elementos que usan este estado, no se puede eliminar
        if (elementosAsociados != null && !elementosAsociados.isEmpty()) {
            return ResponseProvider.error("No se puede eliminar el estado porque tiene elementos asociados", 409);
        }

        // Intentar eliminar el estado de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el estado fue eliminado
            return ResponseProvider.success(null, "Estado eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el estado
            return ResponseProvider.error("Error al eliminar el estado", 500);
        }
    }
}
