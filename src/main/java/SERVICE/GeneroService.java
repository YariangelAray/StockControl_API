package service;

import model.Genero;
import model.dao.GeneroDAO;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.UsuarioDAO;
import model.Usuario;

/**
 * Servicio que maneja la lógica de negocio relacionada con géneros.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerGenero(int id)
 * - crearGenero(Genero genero)
 * - actualizarGenero(int id, Genero genero)
 * - eliminarGenero(int id)
 * 
 * @author Yariangel Aray
 */
public class GeneroService {

    private GeneroDAO dao; // Instancia del DAO para acceder a los datos de géneros

    public GeneroService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new GeneroDAO();
    }

    /**
     * Retorna todos los géneros registrados en la base de datos.
     *
     * @return Lista de géneros o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de géneros desde el DAO
        List<Genero> generos = dao.getAll();

        // Verifica si la lista está vacía
        if (generos.isEmpty()) {
            // Retorna un error si no se encontraron géneros
            return ResponseProvider.error("No se encontraron géneros", 404);
        }

        // Retorna la lista de géneros si se encontraron
        return ResponseProvider.success(generos, "Géneros obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un género por su ID.
     *
     * @param id ID del género.
     * @return Género encontrado o error si no existe.
     */
    public Response obtenerGenero(int id) {
        // Busca el género por ID en el DAO
        Genero genero = dao.getById(id);

        // Verifica si el género fue encontrado
        if (genero == null) {
            // Retorna un error si no se encontró el género
            return ResponseProvider.error("Género no encontrado", 404);
        }

        // Retorna el género si fue encontrado
        return ResponseProvider.success(genero, "Género obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo género.
     *
     * @param genero Objeto con los datos del nuevo género.
     * @return Género creado o mensaje de error si falla el registro.
     */
    public Response crearGenero(Genero genero) {
        // Intentar crear el género en la base de datos
        Genero nuevoGenero = dao.create(genero);
        if (nuevoGenero != null) {
            // Retorna el nuevo género si fue creado correctamente
            return ResponseProvider.success(nuevoGenero, "Género creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el género
            return ResponseProvider.error("Error al crear el género", 400);
        }
    }

    /**
     * Actualiza los datos de un género existente.
     *
     * @param id ID del género a actualizar.
     * @param genero Objeto con los nuevos datos.
     * @return Género actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarGenero(int id, Genero genero) {
        // Validar que el género exista
        Genero generoExistente = dao.getById(id);
        if (generoExistente == null) {
            // Retorna un error si el género no fue encontrado
            return ResponseProvider.error("Género no encontrado", 404);
        }

        // Intentar actualizar el género en la base de datos
        Genero generoActualizado = dao.update(id, genero);
        if (generoActualizado != null) {
            // Retorna el género actualizado si fue exitoso
            return ResponseProvider.success(generoActualizado, "Género actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el género
            return ResponseProvider.error("Error al actualizar el género", 404);
        }
    }

    /**
     * Elimina un género existente por su ID.
     *
     * @param id ID del género a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarGenero(int id) {
        // Verificar existencia del género
        Genero generoExistente = dao.getById(id);
        if (generoExistente == null) {
            // Retorna un error si el género no fue encontrado
            return ResponseProvider.error("Género no encontrado", 404);
        }

        // Se crea una instancia de UsuarioDAO para acceder a los usuarios
        UsuarioDAO usuarioDao = new UsuarioDAO();

        // Se obtiene la lista de usuarios que tienen asignado el género con el ID proporcionado
        List<Usuario> usuarios = usuarioDao.getAllByIdGenero(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay usuarios asociados a ese género)
        if (usuarios != null && !usuarios.isEmpty())
            // Si hay usuarios asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar el género
            return ResponseProvider.error("No se puede eliminar el género porque tiene usuarios asociados", 409);

        // Intentar eliminar el género de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el género fue eliminado
            return ResponseProvider.success(null, "Género eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el género
            return ResponseProvider.error("Error al eliminar el género", 500);
        }
    }
}
