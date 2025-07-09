package model.dao;

import model.entity.Inventario;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'inventarios' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los inventarios.
 * - getById(int id): Busca un inventario por su ID.
 * - create(Inventario inventario): Crea un nuevo inventario y retorna el objeto creado.
 * - update(int id, Inventario inventario): Actualiza un inventario por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un inventario por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class InventarioDAO {

    /**
     * Obtiene todos los registros de inventarios desde la base de datos.
     *
     * @return Lista de objetos Inventario con todos los inventarios registrados.
     */
    public List<Inventario> getAll() {
        // Inicializa una lista para almacenar los inventarios
        List<Inventario> inventarios = new ArrayList<>();
        // Consulta SQL para seleccionar todos los inventarios
        String SQL = "SELECT * FROM inventarios";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Inventario a partir de los datos de la fila actual
                Inventario inventario = new Inventario(
                    rs.getInt("id"), // Obtiene el ID del inventario
                    rs.getString("nombre"), // Obtiene el nombre del inventario
                    rs.getDate("fecha_creacion"), // Obtiene la fecha de creación del inventario
                    rs.getInt("usuario_admin_id") // Obtiene el ID del usuario administrador
                );
                // Agrega el inventario a la lista
                inventarios.add(inventario);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de inventarios
        return inventarios;
    }

    /**
     * Busca un inventario por su ID.
     *
     * @param id El ID del inventario a buscar.
     * @return El objeto Inventario si se encuentra, o null si no existe.
     */
    public Inventario getById(int id) {
        // Inicializa el objeto Inventario como null
        Inventario inventario = null;
        // Consulta SQL para seleccionar un inventario por ID
        String SQL = "SELECT * FROM inventarios WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Inventario a partir de los datos de la fila actual
                inventario = new Inventario(
                    rs.getInt("id"), // Obtiene el ID del inventario
                    rs.getString("nombre"), // Obtiene el nombre del inventario
                        rs.getDate("fecha_creacion"), // Obtiene la fecha de creación del inventario
                    rs.getInt("usuario_admin_id") // Obtiene el ID del usuario administrador
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el inventario encontrado o null si no existe
        return inventario;
    }

    /**
     * Inserta un nuevo inventario en la base de datos.
     *
     * @param inventario Inventario con la información a registrar.
     * @return Inventario creado con el ID generado, o null si hubo error.
     */
    public Inventario create(Inventario inventario) {
        // Consulta SQL para insertar un nuevo inventario
        String SQL = "INSERT INTO inventarios (nombre, fecha_creacion, usuario_admin_id) VALUES (?, ?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, inventario.getNombre());
            stmt.setDate(2, new java.sql.Date(inventario.getFecha_creacion().getTime())); // Asigna fecha
            stmt.setInt(3, inventario.getUsuario_admin_id());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo inventario)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto inventario y lo retorna
                    inventario.setId(generatedKeys.getInt(1));
                    return inventario;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el inventario
        return null;
    }

    /**
     * Actualiza un inventario existente por su ID.
     *
     * @param id ID del inventario a actualizar.
     * @param inventario Objeto Inventario con los nuevos datos.
     * @return Inventario actualizado si fue exitoso, o null si falló.
     */
    public Inventario update(int id, Inventario inventario) {
        // Consulta SQL para actualizar un inventario existente
        String SQL = "UPDATE inventarios SET nombre = ?, fecha_creacion = ?, usuario_admin_id = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, inventario.getNombre());
            stmt.setInt(2, inventario.getUsuario_admin_id());
            stmt.setDate(3, new java.sql.Date(inventario.getFecha_creacion().getTime())); // Asigna fecha
            stmt.setInt(4, id); // Establece el ID del inventario a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto inventario y lo retorna
                inventario.setId(id);
                return inventario;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el inventario
        return null;
    }

    /**
     * Elimina un inventario de la base de datos por su ID.
     *
     * @param id ID del inventario a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un inventario por ID
        String SQL = "DELETE FROM inventarios WHERE id = ?";

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
