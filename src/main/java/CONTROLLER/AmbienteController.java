package controller;

import model.dao.AmbienteDAO;
import model.dao.ElementoDAO;
import model.Ambiente;
import model.Elemento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import model.Centro;
import model.dao.CentroDAO;

/**
 * Controlador REST para gestionar operaciones relacionadas con los ambientes.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar ambientes.
 *
 * Rutas disponibles:
 * - GET /ambientes: Listar todos los ambientes.
 * - GET /ambientes/{id}: Buscar ambiente por ID.
 * - POST /ambientes: Crear nuevo ambiente.
 * - PUT /ambientes/{id}: Actualizar ambiente existente.
 * - DELETE /ambientes/{id}: Eliminar ambiente.
 * 
 * @author Yariangel Aray
 */
@Path("/ambientes")
public class AmbienteController {

    private final AmbienteDAO dao;        // DAO para acceder a la tabla de ambientes
    private final ElementoDAO elementoDao; // DAO para consultar elementos asociados
    private final CentroDAO centroDao; // DAO para validar centro_id

    public AmbienteController() {
        // Inicializa los DAOs
        this.dao = new AmbienteDAO();
        this.elementoDao = new ElementoDAO();
        this.centroDao = new CentroDAO();
    }

    /**
     * Retorna todos los ambientes registrados, incluyendo sus elementos asociados.
     *
     * @return Lista de ambientes o mensaje de error si no se encuentran resultados.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            // Se obtiene la lista de ambientes desde la base de datos
            List<Ambiente> ambientes = dao.getAll();

            // Verifica si no se encontraron ambientes
            if (ambientes.isEmpty()) {
                return ResponseProvider.error("No se encontraron ambientes", 404);
            }

            // Por cada ambiente encontrado, obtener sus elementos asociados
            for (Ambiente ambiente : ambientes) {
                List<Elemento> elementos = elementoDao.getAllByIdAmbiente(ambiente.getId());
                ambiente.setElementos(elementos);
            }

            // Retorna la lista con éxito
            return ResponseProvider.success(ambientes, "Ambientes obtenidos correctamente", 200);

        } catch (Exception e) {
            // En caso de excepción, imprime el error y retorna error 500
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Busca un ambiente específico por su ID, incluyendo sus elementos.
     *
     * @param id Identificador del ambiente.
     * @return Ambiente encontrado o mensaje de error si no existe.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAmbiente(@PathParam("id") int id) {
        try {
            // Busca el ambiente por ID
            Ambiente ambiente = dao.getById(id);

            // Verifica si no fue encontrado
            if (ambiente == null) {
                return ResponseProvider.error("Ambiente no encontrado", 204);
            }

            // Obtiene y asigna los elementos asociados al ambiente
            List<Elemento> elementos = elementoDao.getAllByIdAmbiente(id);
            ambiente.setElementos(elementos);

            // Retorna el ambiente con éxito
            return ResponseProvider.success(ambiente, "Ambiente obtenido correctamente", 200);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Crea un nuevo ambiente con los datos proporcionados.
     * Aplica validación de campos mediante middleware.
     *
     * @param ambiente Objeto JSON recibido desde el cliente.
     * @return Respuesta con el ambiente creado o mensaje de error.
     */
    @POST
    @ValidarCampos(entidad = "ambiente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearAmbiente(Ambiente ambiente) {
        try {
            
            Centro centroExistente = centroDao.getById(ambiente.getCentro_id());
            if (centroExistente == null) {
                return ResponseProvider.error("El centro especificado no existe.", 404);
            }
            
            // Intenta registrar el nuevo ambiente en la base de datos
            Ambiente nuevoAmbiente = dao.create(ambiente);

            // Si el ambiente fue creado correctamente, retorna con éxito
            if (nuevoAmbiente != null) {
                return ResponseProvider.success(nuevoAmbiente, "Ambiente creado correctamente", 201);
            } else {
                // Si ocurrió un error, retorna mensaje de error
                return ResponseProvider.error("Error al crear el ambiente", 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza los datos de un ambiente existente.
     * Valida existencia previa y campos obligatorios.
     *
     * @param id       ID del ambiente a actualizar.
     * @param ambiente Nuevos datos del ambiente.
     * @return Respuesta con el ambiente actualizado o mensaje de error.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "ambiente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarAmbiente(@PathParam("id") int id, Ambiente ambiente) {
        try {                        
            // Verifica que el ambiente exista antes de actualizar
            Ambiente ambienteExistente = dao.getById(id);
            if (ambienteExistente == null) {
                return ResponseProvider.error("Ambiente no encontrado", 404);
            }
            
            Centro centroExistente = centroDao.getById(ambiente.getCentro_id());
            if (centroExistente == null) {
                return ResponseProvider.error("El centro especificado no existe.", 404);
            }

            // Intenta actualizar el ambiente
            Ambiente ambienteActualizado = dao.update(id, ambiente);

            // Verifica si fue exitoso
            if (ambienteActualizado != null) {
                return ResponseProvider.success(ambienteActualizado, "Ambiente actualizado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el ambiente", 404);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un ambiente por su ID, siempre que no tenga elementos asociados.
     *
     * @param id ID del ambiente a eliminar.
     * @return Respuesta indicando si fue eliminado correctamente o si hubo conflicto.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarAmbiente(@PathParam("id") int id) {
        try {
            // Verifica existencia del ambiente
            Ambiente ambienteExistente = dao.getById(id);
            if (ambienteExistente == null) {
                return ResponseProvider.error("Ambiente no encontrado", 404);
            }

            // Obtiene elementos asociados al ambiente
            List<Elemento> elementos = elementoDao.getAllByIdAmbiente(id);

            // Si hay elementos asociados, no se puede eliminar
            if (elementos != null && !elementos.isEmpty()) {
                return ResponseProvider.error("No se puede eliminar el ambiente porque tiene elementos asociados", 409);
            }

            // Intenta eliminar el ambiente
            boolean eliminado = dao.delete(id);

            // Verifica si fue exitoso
            if (eliminado) {
                return ResponseProvider.success(null, "Ambiente eliminado correctamente", 200);
            } else {
                return ResponseProvider.error("Error al eliminar el ambiente", 500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
