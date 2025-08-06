package controller;

import model.dao.EstadoDAO;
import model.dao.ElementoDAO;
import model.Estado;
import model.Elemento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los estados.
 *
 * Rutas disponibles:
 * - GET /estados: Listar todos los estados.
 * - GET /estados/{id}: Buscar estado por ID.
 * - POST /estados: Crear nuevo estado.
 * - PUT /estados/{id}: Actualizar estado existente.
 * - DELETE /estados/{id}: Eliminar estado si no está en uso por elementos.
 *
 * Utiliza @ValidarCampos para validación automática.
 *
 * Autor: Yariangel Aray
 */
@Path("/estados")
public class EstadoController {

    private final EstadoDAO dao;           // DAO para acceder a la tabla estados
    private final ElementoDAO elementoDao; // DAO para verificar si hay elementos con este estado

    public EstadoController() {
        // Inicializa los DAOs necesarios para la lógica
        dao = new EstadoDAO();
        elementoDao = new ElementoDAO();
    }

    /**
     * Obtiene todos los estados registrados.
     *
     * @return Lista de estados o error 404 si no hay resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Consulta todos los estados en la base de datos
            List<Estado> estados = dao.getAll();

            // Verifica si la lista está vacía
            if (estados.isEmpty()) {
                return ResponseProvider.error("No se encontraron estados", 404);
            }

            // Retorna la lista con mensaje de éxito
            return ResponseProvider.success(estados, "Estados obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un estado específico por su ID.
     *
     * @param id ID del estado a buscar.
     * @return Estado encontrado o error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstado(@PathParam("id") int id) {
        try {
            // Busca el estado por ID
            Estado estado = dao.getById(id);

            // Verifica si se encontró
            if (estado == null) {
                return ResponseProvider.error("Estado no encontrado", 404);
            }

            // Retorna el estado
            return ResponseProvider.success(estado, "Estado obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo estado.
     *
     * @param estado Datos del nuevo estado.
     * @return Estado creado o error si falla el proceso.
     */
    @POST
    @ValidarCampos(entidad = "estado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearEstado(Estado estado) {
        try {
            // Intenta crear el estado
            Estado nuevo = dao.create(estado);

            // Verifica si fue exitoso
            if (nuevo != null) {
                return ResponseProvider.success(nuevo, "Estado creado correctamente", 201);
            }

            return ResponseProvider.error("Error al crear el estado", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un estado existente.
     *
     * @param id ID del estado a actualizar.
     * @param estado Datos nuevos para actualizar.
     * @return Estado actualizado o mensaje de error.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "estado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstado(@PathParam("id") int id, Estado estado) {
        try {
            // Verifica existencia
            Estado actual = dao.getById(id);
            if (actual == null) {
                return ResponseProvider.error("Estado no encontrado", 404);
            }

            // Intenta actualizar el estado
            Estado actualizado = dao.update(id, estado);
            if (actualizado != null) {
                return ResponseProvider.success(actualizado, "Estado actualizado correctamente", 200);
            }

            return ResponseProvider.error("Error al actualizar el estado", 404);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un estado del sistema.
     * Solo es posible si no existen elementos asociados a dicho estado.
     *
     * @param id ID del estado a eliminar.
     * @return Mensaje de éxito o error si tiene elementos relacionados.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEstado(@PathParam("id") int id) {
        try {
            // Verifica existencia del estado
            Estado actual = dao.getById(id);
            if (actual == null) {
                return ResponseProvider.error("Estado no encontrado", 404);
            }

            // Consulta si existen elementos asociados a este estado
            List<Elemento> elementos = elementoDao.getByIdTipoEstado(id);
            if (elementos != null && !elementos.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el estado porque tiene elementos asociados", 409);
            }

            // Intenta eliminar el estado
            boolean eliminado = dao.delete(id);
            if (eliminado) {
                return ResponseProvider.success(null, "Estado eliminado correctamente", 200);
            }

            return ResponseProvider.error("Error al eliminar el estado", 500);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
