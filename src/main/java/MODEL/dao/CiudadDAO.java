package model.dao;

import model.entity.Ciudad;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'ciudades' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todas las ciudades.
 * - getById(int id): Busca una ciudad por su ID.
 * - create(Ciudad ciudad): Crea una nueva ciudad y retorna el objeto creado.
 * - update(int id, Ciudad ciudad): Actualiza una ciudad por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina una ciudad por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class CiudadDAO {

    /**
     * Obtiene todos los registros de ciudades desde la base de datos.
     *
     * @return Lista de objetos Ciudad con todas las ciudades registradas.
     */
    public List<Ciudad> getAll() {
        // Inicializa una lista para almacenar las ciudades
        List<Ciudad> ciudades = new ArrayList<>();
        // Consulta SQL para seleccionar todas las ciudades
        String SQL = "SELECT * FROM ciudades";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Ciudad a partir de los datos de la fila actual
                Ciudad ciudad = new Ciudad(
                    rs.getInt("id"), // Obtiene el ID de la ciudad
                    rs.getString("nombre") // Obtiene el nombre de la ciudad
                );
                // Agrega la ciudad a la lista
                ciudades.add(ciudad);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de ciudades
        return ciudades;
    }

    /**
     * Busca una ciudad por su ID.
     *
     * @param id El ID de la ciudad a buscar.
     * @return El objeto Ciudad si se encuentra, o null si no existe.
     */
    public Ciudad getById(int id) {
        // Inicializa el objeto Ciudad como null
        Ciudad ciudad = null;
        // Consulta SQL para seleccionar una ciudad por ID
        String SQL = "SELECT * FROM ciudades WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Ciudad a partir de los datos de la fila actual
                ciudad = new Ciudad(
                    rs.getInt("id"), // Obtiene el ID de la ciudad
                    rs.getString("nombre") // Obtiene el nombre de la ciudad
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la ciudad encontrada o null si no existe
        return ciudad;
    }

    /**
     * Inserta una nueva ciudad en la base de datos.
     *
     * @param ciudad Ciudad con la información a registrar.
     * @return Ciudad creada con el ID generado, o null si hubo error.
     */
    public Ciudad create(Ciudad ciudad) {
        // Consulta SQL para insertar una nueva ciudad
        String SQL = "INSERT INTO ciudades (nombre) VALUES (?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, ciudad.getNombre());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID de la nueva ciudad)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto ciudad y lo retorna
                    ciudad.setId(generatedKeys.getInt(1));
                    return ciudad;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear la ciudad
        return null;
    }

    /**
     * Actualiza una ciudad existente por su ID.
     *
     * @param id ID de la ciudad a actualizar.
     * @param ciudad Objeto Ciudad con los nuevos datos.
     * @return Ciudad actualizada si fue exitoso, o null si falló.
     */
    public Ciudad update(int id, Ciudad ciudad) {
        // Consulta SQL para actualizar una ciudad existente
        String SQL = "UPDATE ciudades SET nombre = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, ciudad.getNombre());
            stmt.setInt(2, id); // Establece el ID de la ciudad a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto ciudad y lo retorna
                ciudad.setId(id);
                return ciudad;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar la ciudad
        return null;
    }

    /**
     * Elimina una ciudad de la base de datos por su ID.
     *
     * @param id ID de la ciudad a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar una ciudad por ID
        String SQL = "DELETE FROM ciudades WHERE id = ?";

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
