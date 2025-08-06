package model.dao;

import model.ProgramaFormacion;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'programas_formacion' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los programas de formación.
 * - getById(int id): Busca un programa por su ID.
 * - create(ProgramaFormacion programa): Crea un nuevo programa y retorna el objeto creado.
 * - update(int id, ProgramaFormacion programa): Actualiza un programa por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un programa por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class ProgramaFormacionDAO {

    /**
     * Obtiene todos los registros de programas desde la base de datos.
     *
     * @return Lista de objetos ProgramaFormacion con todos los programas registrados.
     */
    public List<ProgramaFormacion> getAll() {
        // Inicializa una lista para almacenar los programas
        List<ProgramaFormacion> programas = new ArrayList<>();
        // Consulta SQL para seleccionar todos los programas
        String SQL = "SELECT * FROM programas_formacion";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto ProgramaFormacion a partir de los datos de la fila actual
                ProgramaFormacion programa = new ProgramaFormacion(
                    rs.getInt("id"), // Obtiene el ID del programa
                    rs.getString("nombre") // Obtiene el nombre del programa
                );
                // Agrega el programa a la lista
                programas.add(programa);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de programas
        return programas;
    }

    /**
     * Busca un programa por su ID.
     *
     * @param id El ID del programa a buscar.
     * @return El objeto ProgramaFormacion si se encuentra, o null si no existe.
     */
    public ProgramaFormacion getById(int id) {
        // Inicializa el objeto ProgramaFormacion como null
        ProgramaFormacion programa = null;
        // Consulta SQL para seleccionar un programa por ID
        String SQL = "SELECT * FROM programas_formacion WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto ProgramaFormacion a partir de los datos de la fila actual
                programa = new ProgramaFormacion(
                    rs.getInt("id"), // Obtiene el ID del programa
                    rs.getString("nombre") // Obtiene el nombre del programa
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el programa encontrado o null si no existe
        return programa;
    }

    /**
     * Inserta un nuevo programa en la base de datos.
     *
     * @param programa ProgramaFormacion con la información a registrar.
     * @return ProgramaFormacion creado con el ID generado, o null si hubo error.
     */
    public ProgramaFormacion create(ProgramaFormacion programa) {
        // Consulta SQL para insertar un nuevo programa
        String SQL = "INSERT INTO programas_formacion (nombre) VALUES (?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, programa.getNombre());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo programa)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto programa y lo retorna
                    programa.setId(generatedKeys.getInt(1));
                    return programa;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el programa
        return null;
    }

    /**
     * Actualiza un programa existente por su ID.
     *
     * @param id ID del programa a actualizar.
     * @param programa Objeto ProgramaFormacion con los nuevos datos.
     * @return ProgramaFormacion actualizado si fue exitoso, o null si falló.
     */
    public ProgramaFormacion update(int id, ProgramaFormacion programa) {
        // Consulta SQL para actualizar un programa existente
        String SQL = "UPDATE programas_formacion SET nombre = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, programa.getNombre());
            stmt.setInt(2, id); // Establece el ID del programa a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto programa y lo retorna
                programa.setId(id);
                return programa;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el programa
        return null;
    }

    /**
     * Elimina un programa de la base de datos por su ID.
     *
     * @param id ID del programa a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un programa por ID
        String SQL = "DELETE FROM programas_formacion WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Retorna true si se eliminó al menos un registro
            return filasAfectadas > 0;

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
            return false; // Retorna false si hubo un error
        }
    }
}
