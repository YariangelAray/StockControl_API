package service;

import java.util.ArrayList;
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
     * Obtiene todos los elementos asociados a un inventario específico.
     *
     * @param idInventario ID del inventario.
     * @return Lista de elementos que pertenecen al inventario o mensaje de error si no hay resultados.
     */
    public Response obtenerTodosPorInventario(int idInventario) {
        List<Elemento> elementos = dao.getAllByIdInventario(idInventario); // Llama al DAO

        if (elementos.isEmpty()) {
            return ResponseProvider.error("No se encontraron elementos para este inventario", 404); // Si no hay
        }

        return ResponseProvider.success(elementos, "Elementos del inventario obtenidos correctamente", 200); // Éxito
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
        // Verifica si el serial ya está registrado
        List<Elemento> elementosSerial = dao.getBySerial(elemento.getSerial());
        if (!elementosSerial.isEmpty()) {
            return ResponseProvider.error("Ya existe un elemento con este serial registrado", 409);
        }

        // Verifica si la placa ya está registrada
        List<Elemento> elementosPlaca = dao.getByPlaca(elemento.getPlaca());
        if (!elementosPlaca.isEmpty()) {
            return ResponseProvider.error("Ya existe un elemento con esta placa registrada", 409);
        }
    
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
        
        List<Elemento> elementos = dao.getAll();
        List<Elemento> elementosRegistrados = new ArrayList<>();

        for (Elemento elementoRegistrado : elementos)
            if (elementoRegistrado.getId()!= id) elementosRegistrados.add(elementoRegistrado);

        for (Elemento elementoRegistrado : elementosRegistrados)
            if (elementoRegistrado.getPlaca()== elemento.getPlaca()){
                return ResponseProvider.error("Este número de placa ya fue registrado", 409);
            }

        if (elemento.getSerial() != null && !elemento.getSerial().isEmpty()) {
            for (Elemento elementoRegistrado : elementosRegistrados) {
                if (elemento.getSerial().equals(elementoRegistrado.getSerial())) {
                    return ResponseProvider.error("Este serial ya fue registrado", 409);
                }
            }
        }

        boolean actualizado = dao.update(id, elemento); // Intenta actualizar

        if (actualizado) {
            Elemento actualizadoElemento = dao.getById(id); // Verifica existencia
            return ResponseProvider.success(actualizadoElemento, "Elemento actualizado correctamente", 200); // Éxito
        } else {
            return ResponseProvider.error("Error al actualizar el elemento", 400); // Falla
        }
    }
    
    /**
    * Cambia el estado_activo (true/false) de un elemento.
    *
    * @param id ID del elemento a modificar.
    * @param nuevoEstado Valor booleano a establecer.
    * @return Respuesta con estado de éxito o error.
    */
   public Response actualizarEstado(int id, boolean nuevoEstado) {
       // Verifica que el elemento exista
       Elemento existente = dao.getById(id);
       if (existente == null) {
           return ResponseProvider.error("Elemento no encontrado", 404);
       }

       // Intenta cambiar el estado
       boolean actualizado = dao.updateState(id, nuevoEstado);

       if (actualizado) {
           String mensaje = nuevoEstado
                   ? "Elemento activado correctamente"
                   : "Elemento desactivado correctamente";

           return ResponseProvider.success(null, mensaje, 200);
       } else {
           return ResponseProvider.error("No se pudo actualizar el estado del elemento", 500);
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
