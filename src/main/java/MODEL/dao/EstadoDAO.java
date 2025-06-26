package model.dao;

import model.entity.Estado;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'estados' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los estados.
 * - getById(int id): Busca un estado por su ID.
 * - create(Estado estado): Crea un nuevo estado y retorna el objeto creado.
 * - update(int id, Estado estado): Actualiza un estado por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un estado por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class EstadoDAO {

    /**
     * Obtiene todos los registros de estados desde la base de datos.
     *
     * @return Lista de objetos Estado con todos los estados registrados.
     */
    public List<Estado> getAll() {
        // Inicializa una lista para almacenar los estados
        List<Estado> estados = new ArrayList<>();
        // Consulta SQL para seleccionar todos los estados
        String SQL = "SELECT * FROM estados";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Estado a partir de los datos de la fila actual
                Estado estado = new Estado(
                    rs.getInt("id"), // Obtiene el ID del estado
                    rs.getString("nombre") // Obtiene el nombre del estado
                );
                // Agrega el estado a la lista
                estados.add(estado);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de estados
        return estados;
    }

    /**
     * Busca un estado por su ID.
     *
     * @param id El ID del estado a buscar.
     * @return El objeto Estado si se encuentra, o null si no existe.
     */
    public Estado getById(int id) {
        // Inicializa el objeto Estado como null
        Estado estado = null;
        // Consulta SQL para seleccionar un estado por ID
        String SQL = "SELECT * FROM estados WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Estado a partir de los datos de la fila actual
                estado = new Estado(
                    rs.getInt("id"), // Obtiene el ID del estado
                    rs.getString("nombre") // Obtiene el nombre del estado
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el estado encontrado o null si no existe
        return estado;
    }

    /**
     * Inserta un nuevo estado en la base de datos.
     *
     * @param estado Estado con la información a registrar.
     * @return Estado creado con el ID generado, o null si hubo error.
     */
    public Estado create(Estado estado) {
        // Consulta SQL para insertar un nuevo estado
        String SQL = "INSERT INTO estados (nombre) VALUES (?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, estado.getNombre());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo estado)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto estado y lo retorna
                    estado.setId(generatedKeys.getInt(1));
                    return estado;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el estado
        return null;
    }

    /**
     * Actualiza un estado existente por su ID.
     *
     * @param id ID del estado a actualizar.
     * @param estado Objeto Estado con los nuevos datos.
     * @return Estado actualizado si fue exitoso, o null si falló.
     */
    public Estado update(int id, Estado estado) {
        // Consulta SQL para actualizar un estado existente
        String SQL = "UPDATE estados SET nombre = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, estado.getNombre());
            stmt.setInt(2, id); // Establece el ID del estado a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto estado y lo retorna
                estado.setId(id);
                return estado;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el estado
        return null;
    }

    /**
     * Elimina un estado de la base de datos por su ID.
     *
     * @param id ID del estado a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un estado por ID
        String SQL = "DELETE FROM estados WHERE id = ?";

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
