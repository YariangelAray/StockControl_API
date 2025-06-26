package service;

import model.dao.ElementoDAO;
import model.dao.ReporteDAO;
import model.entity.Elemento;
import model.entity.Reporte;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Servicio que maneja la lógica de negocio relacionada con elementos.
 * Actúa como intermediario entre el controlador (API REST) y la capa DAO.
 * Se encarga de validar reglas antes de enviar los datos a la base de datos.
 *
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerElemento(int id)
 * - crearElemento(Elemento elemento)
 * - actualizarElemento(int id, Elemento elemento)
 * - eliminarElemento(int id)
 *
 * Al eliminar un elemento, verifica que no tenga reportes asociados.
 *
 * Autor: Yariangel Aray
 */
public class ElementoService {

    private ElementoDAO dao; // DAO para acceder a datos de elementos

    public ElementoService() {
        dao = new ElementoDAO(); // Inicializa el DAO
    }

    /**
     * Retorna todos los elementos registrados.
     *
     * @return Lista de elementos o error si no hay.
     */
    public Response obtenerTodos() {
        List<Elemento> elementos = dao.getAll(); // Obtiene elementos

        if (elementos.isEmpty()) {
            return ResponseProvider.error("No se encontraron elementos", 404); // Si está vacía
        }

        return ResponseProvider.success(elementos, "Elementos obtenidos correctamente", 200);
    }

    /**
     * Busca un elemento por su ID.
     *
     * @param id ID del elemento.
     * @return Elemento encontrado o error.
     */
    public Response obtenerElemento(int id) {
        Elemento elemento = dao.getById(id); // Busca por ID

        if (elemento == null) {
            return ResponseProvider.error("Elemento no encontrado", 404); // Si no existe
        }

        return ResponseProvider.success(elemento, "Elemento obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo elemento.
     *
     * @param elemento Datos del nuevo elemento.
     * @return Elemento creado o error.
     */
    public Response crearElemento(Elemento elemento) {
        Elemento nuevoElemento = dao.create(elemento); // Intenta crear

        if (nuevoElemento != null) {
            return ResponseProvider.success(nuevoElemento, "Elemento creado correctamente", 201); // Éxito
        } else {
            return ResponseProvider.error("Error al crear el elemento", 400); // Falla
        }
    }

    /**
     * Actualiza un elemento existente.
     *
     * @param id ID del elemento.
     * @param elemento Datos nuevos.
     * @return Elemento actualizado o error.
     */
    public Response actualizarElemento(int id, Elemento elemento) {
        Elemento existente = dao.getById(id); // Verifica existencia

        if (existente == null) {
            return ResponseProvider.error("Elemento no encontrado", 404); // Si no existe
        }

        Elemento actualizado = dao.update(id, elemento); // Intenta actualizar

        if (actualizado != null) {
            return ResponseProvider.success(actualizado, "Elemento actualizado correctamente", 200); // Éxito
        } else {
            return ResponseProvider.error("Error al actualizar el elemento", 400); // Falla
        }
    }

    /**
     * Elimina un elemento si no tiene reportes asociados.
     *
     * @param id ID del elemento.
     * @return Resultado de la operación.
     */
    public Response eliminarElemento(int id) {
        Elemento existente = dao.getById(id); // Verifica existencia

        if (existente == null) {
            return ResponseProvider.error("Elemento no encontrado", 404); // Si no existe
        }

        ReporteDAO reporteDAO = new ReporteDAO(); // DAO de reportes
        List<Reporte> reportes = reporteDAO.getAllByIdElemento(id); // Obtiene reportes asociados

        if (reportes != null && !reportes.isEmpty()) {
            return ResponseProvider.error("No se puede eliminar el elemento porque tiene reportes asociados", 409); // Conflicto
        }

        boolean eliminado = dao.delete(id); // Intenta eliminar

        if (eliminado) {
            return ResponseProvider.success(null, "Elemento eliminado correctamente", 200); // Éxito
        } else {
            return ResponseProvider.error("Error al eliminar el elemento", 500); // Falla
        }
    }
}
