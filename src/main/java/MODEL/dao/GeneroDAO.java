package model.dao;

import model.Genero;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'generos' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los géneros.
 * - getById(int id): Busca un género por su ID.
 * - create(Genero genero): Crea un nuevo género y retorna el objeto creado.
 * - update(int id, Genero genero): Actualiza un género por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un género por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class GeneroDAO {

    /**
     * Obtiene todos los registros de géneros desde la base de datos.
     *
     * @return Lista de objetos Genero con todos los géneros registrados.
     */
    public List<Genero> getAll() {
        // Inicializa una lista para almacenar los géneros
        List<Genero> generos = new ArrayList<>();
        // Consulta SQL para seleccionar todos los géneros
        String SQL = "SELECT * FROM generos";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Genero a partir de los datos de la fila actual
                Genero genero = new Genero(
                    rs.getInt("id"), // Obtiene el ID del género
                    rs.getString("nombre") // Obtiene el nombre del género
                );
                // Agrega el género a la lista
                generos.add(genero);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de géneros
        return generos;
    }

    /**
     * Busca un género por su ID.
     *
     * @param id El ID del género a buscar.
     * @return El objeto Genero si se encuentra, o null si no existe.
     */
    public Genero getById(int id) {
        // Inicializa el objeto Genero como null
        Genero genero = null;
        // Consulta SQL para seleccionar un género por ID
        String SQL = "SELECT * FROM generos WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Genero a partir de los datos de la fila actual
                genero = new Genero(
                    rs.getInt("id"), // Obtiene el ID del género
                    rs.getString("nombre") // Obtiene el nombre del género
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el género encontrado o null si no existe
        return genero;
    }

    /**
     * Inserta un nuevo género en la base de datos.
     *
     * @param genero Genero con la información a registrar.
     * @return Genero creado con el ID generado, o null si hubo error.
     */
    public Genero create(Genero genero) {
        // Consulta SQL para insertar un nuevo género
        String SQL = "INSERT INTO generos (nombre) VALUES (?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, genero.getNombre());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo género)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto genero y lo retorna
                    genero.setId(generatedKeys.getInt(1));
                    return genero;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el género
        return null;
    }

    /**
     * Actualiza un género existente por su ID.
     *
     * @param id ID del género a actualizar.
     * @param genero Objeto Genero con los nuevos datos.
     * @return Genero actualizado si fue exitoso, o null si falló.
     */
    public Genero update(int id, Genero genero) {
        // Consulta SQL para actualizar un género existente
        String SQL = "UPDATE generos SET nombre = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, genero.getNombre());
            stmt.setInt(2, id); // Establece el ID del género a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto genero y lo retorna
                genero.setId(id);
                return genero;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el género
        return null;
    }

    /**
     * Elimina un género de la base de datos por su ID.
     *
     * @param id ID del género a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un género por ID
        String SQL = "DELETE FROM generos WHERE id = ?";

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
