package service;

import model.TipoDocumento;
import model.dao.TipoDocumentoDAO;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.UsuarioDAO;
import model.Usuario;

/**
 * Servicio que maneja la lógica de negocio relacionada con tipos de documento.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerTipo(int id)
 * - crearTipo(TipoDocumento tipo)
 * - actualizarTipo(int id, TipoDocumento tipo)
 * - eliminarTipo(int id)
 * 
 * @author Yariangel Aray
 */
public class TipoDocumentoService {

    private TipoDocumentoDAO dao; // Instancia del DAO para acceder a los datos de tipos de documento

    public TipoDocumentoService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new TipoDocumentoDAO();
    }

    /**
     * Retorna todos los tipos de documento registrados en la base de datos.
     *
     * @return Lista de tipos o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de tipos de documento desde el DAO
        List<TipoDocumento> tipos = dao.getAll();

        // Verifica si la lista está vacía
        if (tipos.isEmpty()) {
            // Retorna un error si no se encontraron tipos
            return ResponseProvider.error("No se encontraron tipos de documento", 404);
        }

        // Retorna la lista de tipos si se encontraron
        return ResponseProvider.success(tipos, "Tipos de documento obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un tipo de documento por su ID.
     *
     * @param id ID del tipo de documento.
     * @return Tipo encontrado o error si no existe.
     */
    public Response obtenerTipo(int id) {
        // Busca el tipo de documento por ID en el DAO
        TipoDocumento tipo = dao.getById(id);

        // Verifica si el tipo fue encontrado
        if (tipo == null) {
            // Retorna un error si no se encontró el tipo
            return ResponseProvider.error("Tipo de documento no encontrado", 404);
        }

        // Retorna el tipo si fue encontrado
        return ResponseProvider.success(tipo, "Tipo de documento obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo tipo de documento.
     *
     * @param tipo Objeto con los datos del nuevo tipo.
     * @return Tipo creado o mensaje de error si falla el registro.
     */
    public Response crearTipo(TipoDocumento tipo) {
        // Intentar crear el tipo en la base de datos
        TipoDocumento nuevoTipo = dao.create(tipo);
        if (nuevoTipo != null) {
            // Retorna el nuevo tipo si fue creado correctamente
            return ResponseProvider.success(nuevoTipo, "Tipo de documento creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el tipo
            return ResponseProvider.error("Error al crear el tipo de documento", 400);
        }
    }

    /**
     * Actualiza los datos de un tipo de documento existente.
     *
     * @param id ID del tipo a actualizar.
     * @param tipo Objeto con los nuevos datos.
     * @return Tipo actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarTipo(int id, TipoDocumento tipo) {
        // Validar que el tipo exista
        TipoDocumento tipoExistente = dao.getById(id);
        if (tipoExistente == null) {
            // Retorna un error si el tipo no fue encontrado
            return ResponseProvider.error("Tipo de documento no encontrado", 404);
        }

        // Intentar actualizar el tipo en la base de datos
        TipoDocumento tipoActualizado = dao.update(id, tipo);
        if (tipoActualizado != null) {
            // Retorna el tipo actualizado si fue exitoso
            return ResponseProvider.success(tipoActualizado, "Tipo de documento actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el tipo
            return ResponseProvider.error("Error al actualizar el tipo de documento", 404);
        }
    }

    /**
     * Elimina un tipo de documento existente por su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarTipo(int id) {
        // Verificar existencia del tipo de documento
        TipoDocumento tipoExistente = dao.getById(id);
        if (tipoExistente == null) {
            // Retorna un error si el tipo no fue encontrado
            return ResponseProvider.error("Tipo de documento no encontrado", 404);
        }
        
        // Se crea una instancia de UsuarioDAO para acceder a los usuarios
        UsuarioDAO usuarioDao = new UsuarioDAO();

        // Se obtiene la lista de usuarios que tienen asignado este tipo de documento
        List<Usuario> usuarios = usuarioDao.getAllByIdTipoDocumento(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay usuarios asociados)
        if (usuarios != null && !usuarios.isEmpty())
            // Si hay usuarios asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar el rol
            return ResponseProvider.error("No se puede eliminar el tipo de documento porque tiene usuarios asociados", 409);

        // Intentar eliminar el tipo de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el tipo fue eliminado
            return ResponseProvider.success(null, "Tipo de documento eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el tipo
            return ResponseProvider.error("Error al eliminar el tipo de documento", 500);
        }
    }
}
