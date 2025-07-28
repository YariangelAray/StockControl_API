package model.dao;

import model.entity.Centro;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'centros' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los centros.
 * - getById(int id): Busca un centro por su ID.
 * - getAllByCiudadId(int ciudadId): Obtiene todos los centros de una ciudad específica.
 * - create(Centro centro): Crea un nuevo centro y retorna el objeto creado.
 * - update(int id, Centro centro): Actualiza un centro por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un centro por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class CentroDAO {

    /**
     * Obtiene todos los registros de centros desde la base de datos.
     *
     * @return Lista de objetos Centro con todos los centros registrados.
     */
    public List<Centro> getAll() {
        // Inicializa una lista para almacenar los centros
        List<Centro> centros = new ArrayList<>();
        // Consulta SQL para seleccionar todos los centros
        String SQL = "SELECT * FROM centros";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Centro a partir de los datos de la fila actual
                Centro centro = new Centro(
                    rs.getInt("id"), // Obtiene el ID del centro
                    rs.getString("nombre"), // Obtiene el nombre del centro
                    rs.getString("direccion") // Obtiene la dirección del centro                    
                );
                // Agrega el centro a la lista
                centros.add(centro);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de centros
        return centros;
    }

    /**
     * Busca un centro por su ID.
     *
     * @param id El ID del centro a buscar.
     * @return El objeto Centro si se encuentra, o null si no existe.
     */
    public Centro getById(int id) {
        // Inicializa el objeto Centro como null
        Centro centro = null;
        // Consulta SQL para seleccionar un centro por ID
        String SQL = "SELECT * FROM centros WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Centro a partir de los datos de la fila actual
                centro = new Centro(
                    rs.getInt("id"), // Obtiene el ID del centro
                    rs.getString("nombre"), // Obtiene el nombre
                    rs.getString("direccion") // Obtiene la dirección                    
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el centro encontrado o null si no existe
        return centro;
    }
   

    /**
     * Inserta un nuevo centro en la base de datos.
     *
     * @param centro Centro con la información a registrar.
     * @return Centro creado con el ID generado, o null si hubo error.
     */
    public Centro create(Centro centro) {
        // Consulta SQL para insertar un nuevo centro
        String SQL = "INSERT INTO centros (nombre, direccion) VALUES (?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, centro.getNombre());
            stmt.setString(2, centro.getDireccion());            

            // Ejecuta la consulta y verifica las filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                // Obtiene el ID generado y lo asigna al objeto
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    centro.setId(generatedKeys.getInt(1));
                    return centro;
                }
            }

        } catch (SQLException e) {
            // Muestra el error
            e.printStackTrace();
        }

        // Retorna null si hubo fallo
        return null;
    }

    /**
     * Actualiza un centro existente por su ID.
     *
     * @param id ID del centro a actualizar.
     * @param centro Objeto Centro con los nuevos datos.
     * @return Centro actualizado si fue exitoso, o null si falló.
     */
    public Centro update(int id, Centro centro) {
        // Consulta SQL para actualizar los datos
        String SQL = "UPDATE centros SET nombre = ?, direccion = ?, WHERE id = ?";

        // Intenta ejecutar la operación
        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            // Asigna los valores a la consulta
            stmt.setString(1, centro.getNombre());
            stmt.setString(2, centro.getDireccion());            
            stmt.setInt(3, id);

            // Verifica si fue actualizado
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                centro.setId(id);
                return centro;
            }

        } catch (SQLException e) {
            // Imprime el error
            e.printStackTrace();
        }

        // Retorna null si no se pudo actualizar
        return null;
    }

    /**
     * Elimina un centro de la base de datos por su ID.
     *
     * @param id ID del centro a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un centro
        String SQL = "DELETE FROM centros WHERE id = ?";

        // Intenta ejecutar la eliminación
        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            // Imprime el error
            e.printStackTrace();
            return false;
        }
    }
}
