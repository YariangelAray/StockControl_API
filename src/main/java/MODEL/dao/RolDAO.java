package model.dao;

import model.entity.Rol;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'roles' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los roles.
 * - getById(int id): Busca un rol por su ID.
 * - create(Rol rol): Crea un nuevo rol y retorna el objeto creado.
 * - update(int id, Rol rol): Actualiza un rol por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un rol por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class RolDAO {

    /**
     * Obtiene todos los registros de roles desde la base de datos.
     *
     * @return Lista de objetos Rol con todos los roles registrados.
     */
    public List<Rol> getAll() {
        // Inicializa una lista para almacenar los roles
        List<Rol> roles = new ArrayList<>();
        // Consulta SQL para seleccionar todos los roles
        String SQL = "SELECT * FROM roles";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Rol a partir de los datos de la fila actual
                Rol rol = new Rol(
                    rs.getInt("id"), // Obtiene el ID del rol
                    rs.getString("nombre"), // Obtiene el nombre del rol
                    rs.getString("descripcion") // Obtiene la descripcion del rol
                );
                // Agrega el rol a la lista
                roles.add(rol);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de roles
        return roles;
    }

    /**
     * Busca un rol por su ID.
     *
     * @param id El ID del rol a buscar.
     * @return El objeto Rol si se encuentra, o null si no existe.
     */
    public Rol getById(int id) {
        // Inicializa el objeto Rol como null
        Rol rol = null;
        // Consulta SQL para seleccionar un rol por ID
        String SQL = "SELECT * FROM roles WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Rol a partir de los datos de la fila actual
                rol = new Rol(
                    rs.getInt("id"), // Obtiene el ID del rol
                    rs.getString("nombre"), // Obtiene el nombre del rol
                    rs.getString("descripcion") // Obtiene la descripcion del rol
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el rol encontrado o null si no existe
        return rol;
    }

    /**
     * Inserta un nuevo rol en la base de datos.
     *
     * @param rol Rol con la información a registrar.
     * @return Rol creado con el ID generado, o null si hubo error.
     */
    public Rol create(Rol rol) {
        // Consulta SQL para insertar un nuevo rol
        String SQL = "INSERT INTO roles (nombre, descripcion) VALUES (?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo rol)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto rol y lo retorna
                    rol.setId(generatedKeys.getInt(1));
                    return rol;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el rol
        return null;
    }

    /**
     * Actualiza un rol existente por su ID.
     *
     * @param id ID del rol a actualizar.
     * @param rol Objeto Rol con los nuevos datos.
     * @return Rol actualizado si fue exitoso, o null si falló.
     */
    public Rol update(int id, Rol rol) {
        // Consulta SQL para actualizar un rol existente
        String SQL = "UPDATE roles SET nombre = ?, descripcion = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
            stmt.setInt(3, id); // Establece el ID del rol a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto rol y lo retorna
                rol.setId(id);
                return rol;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el rol
        return null;
    }

    /**
     * Elimina un rol de la base de datos por su ID.
     *
     * @param id ID del rol a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un rol por ID
        String SQL = "DELETE FROM roles WHERE id = ?";

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
