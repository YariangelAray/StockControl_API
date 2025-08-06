package service;

import model.Ambiente;
import model.dao.AmbienteDAO;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dao.ElementoDAO;
import model.Elemento;

/**
 * Servicio que maneja la lógica de negocio relacionada con ambientes.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerAmbiente(int id)
 * - crearAmbiente(Ambiente ambiente)
 * - actualizarAmbiente(int id, Ambiente ambiente)
 * - eliminarAmbiente(int id)
 * 
 * @author Yariangel Aray
 */
public class AmbienteService {

    private AmbienteDAO dao; // Instancia del DAO para acceder a los datos de ambientes

    public AmbienteService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new AmbienteDAO();
    }

    /**
     * Retorna todos los ambientes registrados en la base de datos.
     *
     * @return Lista de ambientes o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de ambientes desde el DAO
        List<Ambiente> ambientes = dao.getAll();

        // Verifica si la lista está vacía
        if (ambientes.isEmpty()) {
            // Retorna un error si no se encontraron ambientes
            return ResponseProvider.error("No se encontraron ambientes", 404);
        }
        
        // Instancia el DAO de elemento para poder consultar los elementos asociadas a cada ambiente
        ElementoDAO elementoDAO = new ElementoDAO();

        // Recorre cada ambiente de la lista
        for (Ambiente ambiente : ambientes) {
            // Obtiene los elementos relacionados al ambiente actual (por su ID)
            List<Elemento> elementos = elementoDAO.getAllByIdAmbiente(ambiente.getId());

            // Asigna esos elementos al ambiente (esto llena el atributo 'elementos' de la entidad)
            ambiente.setElementos(elementos);
        }

        // Retorna la lista de ambientes si se encontraron
        return ResponseProvider.success(ambientes, "Ambientes obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un ambiente por su ID.
     *
     * @param id ID del ambiente.
     * @return Ambiente encontrado o error si no existe.
     */
    public Response obtenerAmbiente(int id) {
        // Busca el ambiente por ID en el DAO
        Ambiente ambiente = dao.getById(id);

        // Verifica si el ambiente fue encontrado
        if (ambiente == null) {
            // Retorna un error si no se encontró el ambiente
            return ResponseProvider.error("Ambiente no encontrado", 204);
        }
        
        // Instancia el DAO de elemento para poder consultar los elementos asociadas al ambiente
        ElementoDAO elementoDAO = new ElementoDAO();
        // Obtiene los elementos relacionados al ambiente (por su ID)
        List<Elemento> elementos = elementoDAO.getAllByIdAmbiente(ambiente.getId());
        // Asigna esos elementos al inventario (esto llena el atributo 'elementos' de la entidad)
        ambiente.setElementos(elementos);     

        // Retorna el ambiente si fue encontrado
        return ResponseProvider.success(ambiente, "Ambiente obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo ambiente.
     *
     * @param ambiente Objeto con los datos del nuevo ambiente.
     * @return Ambiente creado o mensaje de error si falla el registro.
     */
    public Response crearAmbiente(Ambiente ambiente) {
        // Intentar crear el ambiente en la base de datos
        Ambiente nuevoAmbiente = dao.create(ambiente);
        if (nuevoAmbiente != null) {
            // Retorna el nuevo ambiente si fue creado correctamente
            return ResponseProvider.success(nuevoAmbiente, "Ambiente creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el ambiente
            return ResponseProvider.error("Error al crear el ambiente", 400);
        }
    }

    /**
     * Actualiza los datos de un ambiente existente.
     *
     * @param id ID del ambiente a actualizar.
     * @param ambiente Objeto con los nuevos datos.
     * @return Ambiente actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarAmbiente(int id, Ambiente ambiente) {
        // Validar que el ambiente exista
        Ambiente ambienteExistente = dao.getById(id);
        if (ambienteExistente == null) {
            // Retorna un error si el ambiente no fue encontrado
            return ResponseProvider.error("Ambiente no encontrado", 404);
        }

        // Intentar actualizar el ambiente en la base de datos
        Ambiente ambienteActualizado = dao.update(id, ambiente);
        if (ambienteActualizado != null) {
            // Retorna el ambiente actualizado si fue exitoso
            return ResponseProvider.success(ambienteActualizado, "Ambiente actualizado correctamente", 200);
        } else {
            System.out.println("holaa");
            // Retorna un error si hubo un problema al actualizar el ambiente
            return ResponseProvider.error("Error al actualizar el ambiente", 404);
        }
    }

    /**
     * Elimina un ambiente existente por su ID.
     *
     * @param id ID del ambiente a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarAmbiente(int id) {
        // Verificar existencia del ambiente
        Ambiente ambienteExistente = dao.getById(id);
        if (ambienteExistente == null) {
            // Retorna un error si el ambiente no fue encontrado
            return ResponseProvider.error("Ambiente no encontrado", 404);
        }

        // Se crea una instancia de ElementoDAO para acceder a los elementos
        ElementoDAO elementoDao = new ElementoDAO();

        // Se obtiene la lista de elementos que están asociados al ambiente con el ID proporcionado
        List<Elemento> elementos = elementoDao.getAllByIdAmbiente(id);

        // Se verifica si la lista no es nula y no está vacía (es decir, si hay elementos asociados a ese ambiente)
        if (elementos != null && !elementos.isEmpty())
            // Si hay elementos asociados, se retorna un error 409 (conflicto) indicando que no se puede eliminar el ambiente
            return ResponseProvider.error("No se puede eliminar el ambiente porque tiene elementos asociados", 409);

        // Intentar eliminar el ambiente de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el ambiente fue eliminado
            return ResponseProvider.success(null, "Ambiente eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el ambiente
            return ResponseProvider.error("Error al eliminar el ambiente", 500);
        }
    }
}
