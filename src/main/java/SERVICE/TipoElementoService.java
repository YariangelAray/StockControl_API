package service;

import model.TipoElemento;
import model.dao.TipoElementoDAO;
import model.dao.ElementoDAO;
import model.Elemento;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Servicio que maneja la lógica de negocio relacionada con los tipos de elementos.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerTipoElemento(int id)
 * - crearTipoElemento(TipoElemento tipoElemento)
 * - actualizarTipoElemento(int id, TipoElemento tipoElemento)
 * - eliminarTipoElemento(int id)
 * 
 * @author Yariangel Aray
 */
public class TipoElementoService {

    TipoElementoDAO dao; // Instancia del DAO para acceder a los datos de tipo_elemento
    ElementoDAO elementoDao;
    public TipoElementoService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new TipoElementoDAO();
        elementoDao = new ElementoDAO();
    }

    /**
     * Retorna todos los tipos de elementos registrados en la base de datos.
     *
     * @return Lista de tipos o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista desde el DAO
        List<TipoElemento> tipos = dao.getAll();

        // Verifica si la lista está vacía
        if (tipos.isEmpty()) {
            // Retorna un error si no se encontraron registros
            return ResponseProvider.error("No se encontraron tipos de elementos", 404);
        }
        
        for (TipoElemento tipo : tipos) {
            tipo.setCantidadElementos(elementoDao.getAllByIdTipoElemento(tipo.getId()).size());
        }

        // Retorna la lista si se encontraron
        return ResponseProvider.success(tipos, "Tipos de elementos obtenidos correctamente", 200);
    }
    /**
     * Retorna todos los tipos de elementos registrados en la base de datos.
     *
     * @return Lista de tipos o error si no hay resultados.
     */
    public Response obtenerTodosPorInventario(int idInventario) {
        // Obtiene la lista desde el DAO
        List<TipoElemento> tipos = dao.getAll();

        // Verifica si la lista está vacía
        if (tipos.isEmpty()) {
            // Retorna un error si no se encontraron registros
            return ResponseProvider.error("No se encontraron tipos de elementos", 404);
        }
        
        for (TipoElemento tipo : tipos) {
            int cantidadElementos = 0;
            List<Elemento> elementosInventario = elementoDao.getAllByIdInventario(idInventario);          
            for (Elemento elemento : elementosInventario) {
                if (elemento.getTipo_elemento_id() == tipo.getId()) cantidadElementos++;                                
            }
            tipo.setCantidadElementos(cantidadElementos);
        }

        // Retorna la lista si se encontraron
        return ResponseProvider.success(tipos, "Tipos de elementos obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un tipo de elemento por su ID.
     *
     * @param id ID del tipo de elemento.
     * @return TipoElemento encontrado o error si no existe.
     */
    public Response obtenerTipoElemento(int id) {
        // Busca el tipo por ID en el DAO
        TipoElemento tipo = dao.getById(id);

        // Verifica si fue encontrado
        if (tipo == null) {
            // Retorna un error si no se encontró
            return ResponseProvider.error("Tipo de elemento no encontrado", 404);
        }

        // Retorna el tipo si fue encontrado
        return ResponseProvider.success(tipo, "Tipo de elemento obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo tipo de elemento.
     *
     * @param tipoElemento Objeto con los datos del nuevo tipo.
     * @return TipoElemento creado o mensaje de error si falla el registro.
     */
    public Response crearTipoElemento(TipoElemento tipoElemento) {
        // Intentar crear el tipo en la base de datos
        TipoElemento existentePorConsecutivo = dao.getByConsecutivo(tipoElemento.getConsecutivo());
        if (existentePorConsecutivo != null) {
            return ResponseProvider.error("Ya existe un tipo de elemento con este consecutivo", 409);
        }
        
        TipoElemento nuevoTipo = dao.create(tipoElemento);
        if (nuevoTipo != null) {
            // Retorna el nuevo tipo si fue creado correctamente
            return ResponseProvider.success(nuevoTipo, "Tipo de elemento creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el tipo
            return ResponseProvider.error("Error al crear el tipo de elemento", 400);
        }
    }

    /**
     * Actualiza los datos de un tipo de elemento existente.
     *
     * @param id ID del tipo a actualizar.
     * @param tipoElemento Objeto con los nuevos datos.
     * @return TipoElemento actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarTipoElemento(int id, TipoElemento tipoElemento) {
        // Validar que el tipo exista
        TipoElemento tipoExistente = dao.getById(id);
        if (tipoExistente == null) {
            // Retorna un error si no fue encontrado
            return ResponseProvider.error("Tipo de elemento no encontrado", 404);
        }
        
        TipoElemento existentePorConsecutivo = dao.getByConsecutivo(tipoElemento.getConsecutivo());
        if (existentePorConsecutivo != null) {
            return ResponseProvider.error("Ya existe un tipo de elemento con este consecutivo", 409);
        }

        // Intentar actualizar en la base de datos
        TipoElemento tipoActualizado = dao.update(id, tipoElemento);
        if (tipoActualizado != null) {
            // Retorna el tipo actualizado si fue exitoso
            return ResponseProvider.success(tipoActualizado, "Tipo de elemento actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema
            return ResponseProvider.error("Error al actualizar el tipo de elemento", 400);
        }
    }

    /**
     * Elimina un tipo de elemento existente por su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return Mensaje de éxito o error si no existe o si hay elementos asociados.
     */
    public Response eliminarTipoElemento(int id) {
        // Verificar existencia del tipo
        TipoElemento tipoExistente = dao.getById(id);
        if (tipoExistente == null) {
            // Retorna error si no existe
            return ResponseProvider.error("Tipo de elemento no encontrado", 404);
        }

        // Validar si existen elementos relacionados con este tipo     
        List<Elemento> elementos = elementoDao.getAllByIdTipoElemento(id);

        // Si hay elementos asociados, se impide la eliminación
        if (elementos != null && !elementos.isEmpty()) {
            return ResponseProvider.error("No se puede eliminar el tipo de elemento porque tiene elementos asociados", 409);
        }

        // Intentar eliminar
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna éxito
            return ResponseProvider.success(null, "Tipo de elemento eliminado correctamente", 200);
        } else {
            // Retorna error si falla
            return ResponseProvider.error("Error al eliminar el tipo de elemento", 500);
        }
    }
}
