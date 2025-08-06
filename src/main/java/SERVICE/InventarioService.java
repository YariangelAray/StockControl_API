package service;

import model.dao.InventarioDAO;
import model.dao.ElementoDAO;
import model.Inventario;
import model.Elemento;
import utils.ResponseProvider;

import java.util.List;
import javax.ws.rs.core.Response;
import model.dto.AmbienteDTO;

/**
 * Servicio que maneja la lógica de negocio relacionada con inventarios.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerInventario(int id)
 * - crearInventario(Inventario inventario)
 * - actualizarInventario(int id, Inventario inventario)
 * - eliminarInventario(int id)
 * 
 * @author Yariangel Aray
 */
public class InventarioService {

    private InventarioDAO dao; // Instancia del DAO para acceder a los datos de inventarios

    public InventarioService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new InventarioDAO();
    }

    /**
     * Retorna todos los inventarios registrados en la base de datos.
     *
     * @return Lista de inventarios o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de inventarios desde el DAO
        List<Inventario> inventarios = dao.getAll();

        // Verifica si la lista está vacía
        if (inventarios.isEmpty()) {
            // Retorna un error si no se encontraron inventarios
            return ResponseProvider.error("No se encontraron inventarios", 404);
        }
        
        // Instancia el DAO de elemento para poder consultar los elementos asociadas a cada inventario
        ElementoDAO elementoDAO = new ElementoDAO();

        // Recorre cada inventario de la lista
        for (Inventario inventario : inventarios) {
            // Obtiene los elementos relacionados al inventario actual (por su ID)
            List<Elemento> elementos = elementoDAO.getAllByIdInventario(inventario.getId());

            // Asigna esos elementos al inventario (esto llena el atributo 'elementos' de la entidad)
            inventario.setElementos(elementos);
            // Calcular y asignar el valor monetario total del inventario
            double totalValor = 0;
            for (Elemento elemento : elementos) {
                totalValor += elemento.getValor_monetario();  // Usa tu getter del campo
            }
            inventario.setValor_monetario(totalValor);
            inventario.setCantidad_elementos(elementos.size());
            inventario.setAmbientes_cubiertos(dao.getAllAmbientesByInventario(inventario.getId()).size());            
        }

        // Retorna la lista de inventarios si se encontraron
        return ResponseProvider.success(inventarios, "Inventarios obtenidos correctamente", 200);
    }
    /**
     * Retorna todos los inventarios registrados en la base de datos que sean de un usuario administradpr.
     *
     * @return Lista de inventarios o error si no hay resultados.
     */
    public Response obtenerTodosUsuarioAdmin(int id) {
        // Obtiene la lista de inventarios desde el DAO
        List<Inventario> inventarios = dao.getAllByIdUserAdmin(id);

        // Verifica si la lista está vacía
        if (inventarios.isEmpty()) {
            // Retorna un error si no se encontraron inventarios
            return ResponseProvider.error("No se encontraron inventarios", 204);
        }
        
        // Instancia el DAO de elemento para poder consultar los elementos asociadas a cada inventario
        ElementoDAO elementoDAO = new ElementoDAO();

        // Recorre cada inventario de la lista
        for (Inventario inventario : inventarios) {
            // Obtiene los elementos relacionados al inventario actual (por su ID)
            List<Elemento> elementos = elementoDAO.getAllByIdInventario(inventario.getId());

            // Asigna esos elementos al inventario (esto llena el atributo 'elementos' de la entidad)
            inventario.setElementos(elementos);
            
            // Calcular y asignar el valor monetario total del inventario
            double totalValor = 0;
            for (Elemento elemento : elementos) {
                totalValor += elemento.getValor_monetario();  // Usa tu getter del campo
            }
            inventario.setValor_monetario(totalValor);
            inventario.setCantidad_elementos(elementos.size());
            inventario.setAmbientes_cubiertos(dao.getAllAmbientesByInventario(inventario.getId()).size());
        }

        // Retorna la lista de inventarios si se encontraron
        return ResponseProvider.success(inventarios, "Inventarios obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un inventario por su ID.
     *
     * @param id ID del inventario.
     * @return Inventario encontrado o error si no existe.
     */
    public Response obtenerInventario(int id) {
        // Busca el inventario por ID en el DAO
        Inventario inventario = dao.getById(id);

        // Verifica si el inventario fue encontrado
        if (inventario == null) {
            // Retorna un error si no se encontró el inventario
            return ResponseProvider.error("Inventario no encontrado", 404);
        }
        
        // Instancia el DAO de elemento para poder consultar los elementos asociadas al inventario
        ElementoDAO elementoDAO = new ElementoDAO();
        // Obtiene los elementos relacionados al inventario (por su ID)
        List<Elemento> elementos = elementoDAO.getAllByIdInventario(inventario.getId());
        // Asigna esos elementos al inventario (esto llena el atributo 'elementos' de la entidad)
        inventario.setElementos(elementos);           
        
        // Calcular y asignar el valor monetario total del inventario
        double totalValor = 0;
        for (Elemento elemento : elementos) {
            totalValor += elemento.getValor_monetario();  // Usa tu getter del campo            
        }
        inventario.setValor_monetario(totalValor);
        inventario.setCantidad_elementos(elementos.size());
        inventario.setAmbientes_cubiertos(dao.getAllAmbientesByInventario(inventario.getId()).size());
        
        // Retorna el inventario si fue encontrado
        return ResponseProvider.success(inventario, "Inventario obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo inventario.
     *
     * @param inventario Objeto con los datos del nuevo inventario.
     * @return Inventario creado o mensaje de error si falla el registro.
     */
    public Response crearInventario(Inventario inventario) {
        // Intentar crear el inventario en la base de datos
        Inventario nuevoInventario = dao.create(inventario);
        Inventario inventarioCreado = dao.getById(nuevoInventario.getId());
        if (nuevoInventario != null && inventarioCreado != null) {
            // Retorna el nuevo inventario si fue creado correctamente
            return ResponseProvider.success(inventarioCreado, "Inventario creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el inventario
            return ResponseProvider.error("Error al crear el inventario", 400);
        }
    }

    /**
     * Actualiza los datos de un inventario existente.
     *
     * @param id ID del inventario a actualizar.
     * @param inventario Objeto con los nuevos datos.
     * @return Inventario actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarInventario(int id, Inventario inventario) {
        // Validar que el inventario exista
        Inventario inventarioExistente = dao.getById(id);
        if (inventarioExistente == null) {
            // Retorna un error si el inventario no fue encontrado
            return ResponseProvider.error("Inventario no encontrado", 404);
        }

        // Intentar actualizar el inventario en la base de datos
        Inventario inventarioActualizado = dao.update(id, inventario);
        if (inventarioActualizado != null) {
            // Retorna el inventario actualizado si fue exitoso
            return ResponseProvider.success(inventarioActualizado, "Inventario actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el inventario
            return ResponseProvider.error("Error al actualizar el inventario", 404);
        }
    }

    /**
     * Elimina un inventario existente por su ID.
     * Se valida que no tenga elementos asociados antes de proceder.
     *
     * @param id ID del inventario a eliminar.
     * @return Mensaje de éxito o error si no existe o hay elementos asociados.
     */
    public Response eliminarInventario(int id) {
        // Verificar existencia del inventario
        Inventario inventarioExistente = dao.getById(id);
        if (inventarioExistente == null) {
            // Retorna un error si el inventario no fue encontrado
            return ResponseProvider.error("Inventario no encontrado", 404);
        }

        // Se crea una instancia de ElementoDAO para verificar si hay elementos asociados
        ElementoDAO elementoDao = new ElementoDAO();
        List<Elemento> elementos = elementoDao.getAllByIdInventario(id);

        // Se verifica si la lista tiene elementos asociados al inventario
        if (elementos != null && !elementos.isEmpty()) {
            // Si hay elementos asociados, se retorna un error 409 (conflicto)
            return ResponseProvider.error("No se puede eliminar el inventario porque tiene elementos asociados", 409);
        }

        // Intentar eliminar el inventario de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el inventario fue eliminado
            return ResponseProvider.success(null, "Inventario eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el inventario
            return ResponseProvider.error("Error al eliminar el inventario", 500);
        }
    }
    
    /**
     *  Obtiene todos los ambientes que esten cubiertos por un inventario
     *
     * @param idInventario Objeto con los nuevos datos.
     * @return Ambientes cubiertos o mensaje de error si no hay o falla la busqueda.
     */
    public Response obtenerAmbientesPorInventario(int idInventario) {
        List<AmbienteDTO> ambientes = dao.getAllAmbientesByInventario(idInventario);
        if (ambientes.isEmpty()) {            
            return ResponseProvider.error("No se encontraron ambientes", 204);
        }

        return ResponseProvider.success(ambientes, "Ambientes obtenidos correctamente", 200);
    }
}
