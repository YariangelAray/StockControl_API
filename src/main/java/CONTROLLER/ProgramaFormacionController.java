package controller;

import java.util.List;
import model.ProgramaFormacion;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.dao.FichaDAO;
import model.dao.ProgramaFormacionDAO;
import model.Ficha;

/**
 * Controlador REST para gestionar operaciones relacionadas con programas de formación.
 * Define rutas HTTP que permiten consultar, crear, actualizar y eliminar programas.
 *
 * Rutas disponibles:
 * - GET /programas-formacion: Listar todos los programas.
 * - GET /programas-formacion/{id}: Buscar programa por ID.
 * - POST /programas-formacion: Crear nuevo programa.
 * - PUT /programas-formacion/{id}: Actualizar programa existente.
 * - DELETE /programas-formacion/{id}: Eliminar programa.
 *
 * @author Yariangel Aray
 */
@Path("/programas-formacion") // Ruta base para las operaciones de programas de formación
public class ProgramaFormacionController {

    private ProgramaFormacionDAO dao; // DAO para operaciones en la base de datos
    private FichaDAO fichaDao; // DAO para acceder a fichas relacionadas

    public ProgramaFormacionController() {
        dao = new ProgramaFormacionDAO(); // Inicializa el DAO principal
        fichaDao = new FichaDAO(); // Inicializa el DAO de fichas
    }

    /**
     * Obtiene todos los programas de formación registrados en el sistema, incluyendo sus fichas asociadas.
     *
     * @return Una respuesta HTTP que contiene la lista de programas y un código 200 si fue exitoso,
     *         o un mensaje de error con el código correspondiente si no se encontraron datos o ocurrió un error.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodos() {
        try {
            List<ProgramaFormacion> programas = dao.getAll(); // Consulta todos los programas
            if (programas.isEmpty()) return ResponseProvider.error("No se encontraron programas de formación", 404);

            // Asigna fichas a cada programa
            for (ProgramaFormacion programa : programas) {
                List<Ficha> fichas = fichaDao.getAllByIdPrograma(programa.getId());
                programa.setFichas(fichas); // Asigna fichas al programa
            }

            return ResponseProvider.success(programas, "Programas obtenidos correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Obtiene un programa de formación por su ID, incluyendo sus fichas asociadas.
     *
     * @param id ID del programa que se desea consultar.
     * @return Una respuesta HTTP con el programa encontrado y un código 200,
     *         o un mensaje de error con código 404 si no existe el programa.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrograma(@PathParam("id") int id) {
        try {
            ProgramaFormacion programa = dao.getById(id); // Consulta programa por ID
            if (programa == null) return ResponseProvider.error("Programa no encontrado", 404);

            List<Ficha> fichas = fichaDao.getAllByIdPrograma(id); // Fichas relacionadas
            programa.setFichas(fichas); // Asignar fichas al programa

            return ResponseProvider.success(programa, "Programa obtenido correctamente", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Registra un nuevo programa de formación en la base de datos.
     *
     * @param programa Objeto JSON con los datos del nuevo programa.
     * @return Una respuesta HTTP con el programa creado y código 201 si fue exitoso,
     *         o un mensaje de error con código 400 si falló el registro.
     */
    @POST
    @ValidarCampos(entidad = "programa_formacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPrograma(ProgramaFormacion programa) {
        try {
            ProgramaFormacion creado = dao.create(programa); // Inserta nuevo programa
            if (creado != null)
                return ResponseProvider.success(creado, "Programa creado correctamente", 201);
            return ResponseProvider.error("Error al crear el programa", 400);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Actualiza un programa de formación existente en la base de datos.
     *
     * @param id ID del programa que se desea actualizar.
     * @param programa Objeto JSON con los nuevos datos del programa.
     * @return Una respuesta HTTP con el programa actualizado y código 200 si fue exitoso,
     *         o un mensaje de error con código 404 si no se encontró o no pudo actualizar.
     */
    @PUT
    @Path("/{id}")
    @ValidarCampos(entidad = "programa_formacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPrograma(@PathParam("id") int id, ProgramaFormacion programa) {
        try {
            ProgramaFormacion existente = dao.getById(id); // Verifica existencia
            if (existente == null)
                return ResponseProvider.error("Programa no encontrado", 404);

            ProgramaFormacion actualizado = dao.update(id, programa); // Ejecuta la actualización
            if (actualizado != null)
                return ResponseProvider.success(actualizado, "Programa actualizado correctamente", 200);
            return ResponseProvider.error("Error al actualizar el programa", 404);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

    /**
     * Elimina un programa de formación si no tiene fichas asociadas.
     *
     * @param id ID del programa que se desea eliminar.
     * @return Una respuesta HTTP con código 200 si se eliminó correctamente,
     *         409 si el programa tiene fichas asociadas, o 404/500 si no se pudo eliminar.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPrograma(@PathParam("id") int id) {
        try {
            ProgramaFormacion existente = dao.getById(id); // Verifica existencia
            if (existente == null)
                return ResponseProvider.error("Programa no encontrado", 404);

            List<Ficha> fichas = fichaDao.getAllByIdPrograma(id); // Busca fichas asociadas
            if (fichas != null && !fichas.isEmpty())
                return ResponseProvider.error("No se puede eliminar el programa porque tiene fichas asociadas", 409);

            boolean eliminado = dao.delete(id); // Ejecuta eliminación
            if (eliminado)
                return ResponseProvider.success(null, "Programa eliminado correctamente", 200);
            return ResponseProvider.error("Error al eliminar el programa", 500);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
}
