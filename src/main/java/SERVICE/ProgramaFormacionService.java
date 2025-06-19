package service;

import model.dao.FichaDAO;
import model.dao.ProgramaFormacionDAO;
import model.entity.Ficha;
import model.entity.ProgramaFormacion;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Servicio que maneja la lógica de negocio relacionada con programas de formación.
 * Actúa como intermediario entre el controlador (API REST) y la capa de acceso a datos (DAO).
 * Se encarga de validar reglas antes de enviar los datos a la base de datos o devolver resultados.
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerPrograma(int id)
 * - crearPrograma(ProgramaFormacion programa)
 * - actualizarPrograma(int id, ProgramaFormacion programa)
 * - eliminarPrograma(int id)
 * 
 * @author Yariangel Aray
 */
public class ProgramaFormacionService {

    private ProgramaFormacionDAO dao; // Instancia del DAO para acceder a los datos de programas

    public ProgramaFormacionService() {
        // Instancia el DAO que se encarga del acceso directo a la base de datos
        dao = new ProgramaFormacionDAO();
    }

    /**
     * Retorna todos los programas registrados en la base de datos.
     *
     * @return Lista de programas o error si no hay resultados.
     */
    public Response obtenerTodos() {
        // Obtiene la lista de programas desde el DAO
        List<ProgramaFormacion> programas = dao.getAll();

        // Verifica si la lista está vacía
        if (programas.isEmpty()) {
            // Retorna un error si no se encontraron programas
            return ResponseProvider.error("No se encontraron programas de formación", 404);
        }

        // Retorna la lista de programas si se encontraron
        return ResponseProvider.success(programas, "Programas obtenidos correctamente", 200);
    }

    /**
     * Busca y retorna un programa por su ID, incluyendo las fichas asociadas.
     *
     * @param id ID del programa.
     * @return Programa encontrado con sus fichas o error si no existe.
     */
    public Response obtenerPrograma(int id) {
        // Busca el programa por ID en el DAO
        ProgramaFormacion programa = dao.getById(id);

        // Verifica si el programa fue encontrado
        if (programa == null) {
            // Retorna un error si no se encontró el programa
            return ResponseProvider.error("Programa no encontrado", 404);
        }

        // Instancia el DAO de ficha para obtener las fichas asociadas
        FichaDAO fichaDAO = new FichaDAO();
        List<Ficha> fichas = fichaDAO.getAllByIdPrograma(id); // Busca fichas relacionadas

        // Asigna las fichas al programa (requiere setFichas en la entidad)
        programa.setFichas(fichas);

        // Retorna el programa con sus fichas
        return ResponseProvider.success(programa, "Programa obtenido correctamente", 200);
    }

    /**
     * Crea un nuevo programa.
     *
     * @param programa Objeto con los datos del nuevo programa.
     * @return Programa creado o mensaje de error si falla el registro.
     */
    public Response crearPrograma(ProgramaFormacion programa) {
        // Intentar crear el programa en la base de datos
        ProgramaFormacion nuevoPrograma = dao.create(programa);
        if (nuevoPrograma != null) {
            // Retorna el nuevo programa si fue creado correctamente
            return ResponseProvider.success(nuevoPrograma, "Programa creado correctamente", 201);
        } else {
            // Retorna un error si hubo un problema al crear el programa
            return ResponseProvider.error("Error al crear el programa", 400);
        }
    }

    /**
     * Actualiza los datos de un programa existente.
     *
     * @param id ID del programa a actualizar.
     * @param programa Objeto con los nuevos datos.
     * @return Programa actualizado o mensaje de error si no existe o falla la actualización.
     */
    public Response actualizarPrograma(int id, ProgramaFormacion programa) {
        // Validar que el programa exista
        ProgramaFormacion programaExistente = dao.getById(id);
        if (programaExistente == null) {
            // Retorna un error si el programa no fue encontrado
            return ResponseProvider.error("Programa no encontrado", 404);
        }

        // Intentar actualizar el programa en la base de datos
        ProgramaFormacion programaActualizado = dao.update(id, programa);
        if (programaActualizado != null) {
            // Retorna el programa actualizado si fue exitoso
            return ResponseProvider.success(programaActualizado, "Programa actualizado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al actualizar el programa
            return ResponseProvider.error("Error al actualizar el programa", 404);
        }
    }

    /**
     * Elimina un programa existente por su ID.
     *
     * @param id ID del programa a eliminar.
     * @return Mensaje de éxito o error si no existe o falla la eliminación.
     */
    public Response eliminarPrograma(int id) {
        // Verificar existencia del programa
        ProgramaFormacion programaExistente = dao.getById(id);
        if (programaExistente == null) {
            // Retorna un error si el programa no fue encontrado
            return ResponseProvider.error("Programa no encontrado", 404);
        }

        // Se crea una instancia de FichaDAO para acceder a las fichas asociadas
        FichaDAO fichaDao = new FichaDAO();

        // Se obtiene la lista de fichas que están asociadas al programa
        List<Ficha> fichas = fichaDao.getAllByIdPrograma(id);

        // Verifica si hay fichas relacionadas
        if (fichas != null && !fichas.isEmpty()) {
            // Si hay fichas asociadas, retorna un error 409 (conflicto)
            return ResponseProvider.error("No se puede eliminar el programa porque tiene fichas asociadas", 409);
        }

        // Intentar eliminar el programa de la base de datos
        boolean eliminado = dao.delete(id);
        if (eliminado) {
            // Retorna un mensaje de éxito si el programa fue eliminado
            return ResponseProvider.success(null, "Programa eliminado correctamente", 200);
        } else {
            // Retorna un error si hubo un problema al eliminar el programa
            return ResponseProvider.error("Error al eliminar el programa", 500);
        }
    }
}
