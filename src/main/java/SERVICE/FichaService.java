package service;

import model.dao.FichaDAO;
import model.dao.UsuarioDAO;
import model.entity.Ficha;
import model.entity.Usuario;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Servicio que maneja la lógica de negocio relacionada con las fichas.
 * Se encarga de validar reglas antes de enviar o modificar datos en la base de datos.
 * 
 * Métodos disponibles:
 * - obtenerTodas()
 * - obtenerFicha(int id)
 * - crearFicha(Ficha ficha)
 * - actualizarFicha(int id, Ficha ficha)
 * - eliminarFicha(int id)
 * 
 * Antes de eliminar, valida que no haya usuarios asociados a esa ficha.
 * 
 * @author Yariangel Aray
 */
public class FichaService {

    // Instancia del DAO de fichas para acceder a los datos en la base
    private FichaDAO dao;

    // Constructor del servicio que inicializa el DAO
    public FichaService() {
        dao = new FichaDAO();
    }

    /**
     * Obtiene todas las fichas registradas en la base de datos.
     *
     * @return Lista de fichas o mensaje de error si no hay ninguna.
     */
    public Response obtenerTodas() {
        // Se obtiene la lista completa de fichas desde el DAO
        List<Ficha> fichas = dao.getAll();

        // Si la lista está vacía, se retorna error 404
        if (fichas.isEmpty()) {
            return ResponseProvider.error("No se encontraron fichas registradas", 404);
        }

        // Si hay resultados, se retorna éxito con la lista
        return ResponseProvider.success(fichas, "Fichas obtenidas correctamente", 200);
    }

    /**
     * Obtiene una ficha por su ID único.
     *
     * @param id ID de la ficha a buscar.
     * @return Ficha encontrada o error si no existe.
     */
    public Response obtenerFicha(int id) {
        // Se busca la ficha por su ID
        Ficha ficha = dao.getById(id);

        // Si no se encuentra, se retorna error 404
        if (ficha == null) {
            return ResponseProvider.error("Ficha no encontrada", 404);
        }

        // Si se encuentra, se retorna con éxito
        return ResponseProvider.success(ficha, "Ficha obtenida correctamente", 200);
    }

    /**
     * Crea una nueva ficha en la base de datos.
     *
     * @param ficha Objeto con los datos de la nueva ficha.
     * @return Ficha creada o error si ocurre una falla.
     */
    public Response crearFicha(Ficha ficha) {
        // Se intenta insertar la ficha usando el DAO
        Ficha nuevaFicha = dao.create(ficha);

        // Si se creó correctamente, se retorna con éxito
        if (nuevaFicha != null) {
            return ResponseProvider.success(nuevaFicha, "Ficha creada correctamente", 201);
        }

        // Si falló la inserción, se retorna error 400
        return ResponseProvider.error("Error al crear la ficha", 400);
    }

    /**
     * Actualiza los datos de una ficha existente.
     *
     * @param id ID de la ficha a actualizar.
     * @param ficha Nuevos datos de la ficha.
     * @return Ficha actualizada o mensaje de error si falla.
     */
    public Response actualizarFicha(int id, Ficha ficha) {
        // Primero se verifica que la ficha exista
        Ficha fichaExistente = dao.getById(id);
        if (fichaExistente == null) {
            // Si no existe, se retorna error 404
            return ResponseProvider.error("Ficha no encontrada", 404);
        }

        // Se intenta actualizar la ficha
        Ficha fichaActualizada = dao.update(id, ficha);
        if (fichaActualizada != null) {
            // Si se actualizó correctamente, se retorna éxito
            return ResponseProvider.success(fichaActualizada, "Ficha actualizada correctamente", 200);
        }

        // Si hubo error al actualizar, se retorna error 400
        return ResponseProvider.error("Error al actualizar la ficha", 400);
    }

    /**
     * Elimina una ficha si no tiene usuarios asociados.
     *
     * @param id ID de la ficha a eliminar.
     * @return Mensaje de éxito o error dependiendo del resultado.
     */
    public Response eliminarFicha(int id) {
        // Se verifica si la ficha existe en la base de datos
        Ficha fichaExistente = dao.getById(id);
        if (fichaExistente == null) {
            // Si no existe, se retorna error 404
            return ResponseProvider.error("Ficha no encontrada", 404);
        }

        // Se instancia el DAO de usuarios para buscar si hay usuarios con esta ficha
        UsuarioDAO usuarioDao = new UsuarioDAO();

        // Se obtiene la lista de usuarios que pertenecen a esta ficha
        List<Usuario> usuarios = usuarioDao.getAllByIdFicha(id);

        // Si hay usuarios asociados, se retorna error 409 (conflicto)
        if (usuarios != null && !usuarios.isEmpty()) {
            return ResponseProvider.error("No se puede eliminar la ficha porque tiene usuarios asociados", 409);
        }

        // Si no hay usuarios asociados, se intenta eliminar la ficha
        boolean eliminada = dao.delete(id);
        if (eliminada) {
            // Si la eliminación fue exitosa, se retorna éxito
            return ResponseProvider.success(null, "Ficha eliminada correctamente", 200);
        } else {
            // Si falló la eliminación, se retorna error 500
            return ResponseProvider.error("Error al eliminar la ficha", 500);
        }
    }
}
