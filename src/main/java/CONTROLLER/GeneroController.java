package controller;

// Importaciones necesarias para validación, manejo de datos y respuestas
import model.Genero;
import model.Usuario;
import model.dao.GeneroDAO;
import model.dao.UsuarioDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los géneros.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar géneros.
 *
 * Rutas disponibles:
 * - GET /generos: Listar todos los géneros.
 * - GET /generos/{id}: Buscar género por ID.
 * - POST /generos: Crear nuevo género.
 * - PUT /generos/{id}: Actualizar género existente.
 * - DELETE /generos/{id}: Eliminar género si no tiene usuarios asociados.
 * 
 * Autor: Yariangel Aray
 */
@Path("/generos") // Ruta base del recurso
public class GeneroController {

    // DAO para interactuar con la base de datos
    private final GeneroDAO dao;
    private final UsuarioDAO usuarioDao;

    // Constructor que inicializa el DAO
    public GeneroController() {
        dao = new GeneroDAO();
        usuarioDao = new UsuarioDAO();
    }

    /**
     * Obtiene todos los géneros registrados en la base de datos.
     *
     * @return Lista de géneros o mensaje de error si no hay ninguno.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            List<Genero> generos = dao.getAll();
            if (generos.isEmpty()) {
                return ResponseProvider.error("No se encontraron géneros", 404);
            }
            return ResponseProvider.success(generos, "Géneros obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un género por su ID único.
     *
     * @param id ID del género a buscar.
     * @return Género encontrado o mensaje de error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerGenero(@PathParam("id") int id) {
        try {
            Genero genero = dao.getById(id);
            if (genero == null) {
                return ResponseProvider.error("Género no encontrado", 404);
            }
            return ResponseProvider.success(genero, "Género obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo género en la base de datos.
     *
     * @param genero Objeto con los datos del nuevo género.
     * @return Género creado o mensaje de error si ocurre un fallo.
     */
    @POST
    @ValidarCampos(entidad = "genero")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearGenero(Genero genero) {
        try {
            Genero nuevoGenero = dao.create(genero);
            if (nuevoGenero != null) {
                return ResponseProvider.success(nuevoGenero, "Género creado correctamente", 201);
            } else {
                return ResponseProvider.error("Error al crear el género", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un género existente.
     *
     * @param id ID del género a actualizar.
     * @param genero Nuevos datos del género.
     * @return Género actualizado o mensaje de error si falla.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "genero")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarGenero(@PathParam("id") int id, Genero genero) {
        try {
            Genero existente = dao.getById(id);
            if (existente == null) {
                return ResponseProvider.error("Género no encontrado", 404);
            }

            Genero actualizado = dao.update(id, genero);
            if (actualizado != null) {
                return ResponseProvider.success(actualizado, "Género actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el género", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un género por su ID, solo si no tiene usuarios asociados.
     *
     * @param id ID del género a eliminar.
     * @return Mensaje de éxito o error si falla.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarGenero(@PathParam("id") int id) {
        try {
            // Verifica que el género exista
            Genero generoExistente = dao.getById(id);
            if (generoExistente == null) {
                return ResponseProvider.error("Género no encontrado", 404);
            }

            // Verifica si hay usuarios asociados a ese género           
            List<Usuario> usuarios = usuarioDao.getAllByIdGenero(id);
            if (usuarios != null && !usuarios.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el género porque tiene usuarios asociados", 409);
            }

            // Intenta eliminar el género
            boolean eliminado = dao.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Género eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el género", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
