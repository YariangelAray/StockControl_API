package model.dao;

import model.entity.Ambiente;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'ambientes' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los ambientes.
 * - getById(int id): Busca un ambiente por su ID.
 * - create(Ambiente ambiente): Crea un nuevo ambiente y retorna el objeto creado.
 * - update(int id, Ambiente ambiente): Actualiza un ambiente por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un ambiente por su ID.
 * - getAllByCentroId(int id): Obtiene todos los ambientes asociados a un centro específico.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class AmbienteDAO {

    /**
     * Obtiene todos los registros de ambientes desde la base de datos.
     *
     * @return Lista de objetos Ambiente con todos los ambientes registrados.
     */
    public List<Ambiente> getAll() {
        // Inicializa una lista para almacenar los ambientes
        List<Ambiente> ambientes = new ArrayList<>();
        // Consulta SQL para seleccionar todos los ambientes
        String SQL = "SELECT * FROM ambientes ORDER BY id DESC";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Ambiente a partir de los datos de la fila actual
                Ambiente ambiente = new Ambiente(
                    rs.getInt("id"), // Obtiene el ID del ambiente
                    rs.getString("nombre"), // Obtiene el nombre del ambiente
                    rs.getInt("centro_id"), // Obtiene el ID del centro asociado
                    rs.getString("mapa") // Obtiene el mapa del ambiente
                );
                // Agrega el ambiente a la lista
                ambientes.add(ambiente);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de ambientes
        return ambientes;
    }

    /**
     * Busca un ambiente por su ID.
     *
     * @param id El ID del ambiente a buscar.
     * @return El objeto Ambiente si se encuentra, o null si no existe.
     */
    public Ambiente getById(int id) {
        // Inicializa el objeto Ambiente como null
        Ambiente ambiente = null;
        // Consulta SQL para seleccionar un ambiente por ID
        String SQL = "SELECT * FROM ambientes WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Ambiente a partir de los datos de la fila actual
                ambiente = new Ambiente(
                    rs.getInt("id"), // Obtiene el ID del ambiente
                    rs.getString("nombre"), // Obtiene el nombre del ambiente
                    rs.getInt("centro_id"), // Obtiene el ID del centro asociado
                    rs.getString("mapa") // Obtiene el mapa del ambiente
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el ambiente encontrado o null si no existe
        return ambiente;
    }

    /**
     * Inserta un nuevo ambiente en la base de datos.
     *
     * @param ambiente Ambiente con la información a registrar.
     * @return Ambiente creado con el ID generado, o null si hubo error.
     */
    public Ambiente create(Ambiente ambiente) {
        // Consulta SQL para insertar un nuevo ambiente
        String SQL = "INSERT INTO ambientes (nombre, centro_id, mapa) VALUES (?, ?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, ambiente.getNombre());
            stmt.setInt(2, ambiente.getCentro_id());
            stmt.setString(3, ambiente.getMapa());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo ambiente)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto ambiente y lo retorna
                    ambiente.setId(generatedKeys.getInt(1));
                    return ambiente;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el ambiente
        return null;
    }

    /**
     * Actualiza un ambiente existente por su ID.
     *
     * @param id ID del ambiente a actualizar.
     * @param ambiente Objeto Ambiente con los nuevos datos.
     * @return Ambiente actualizado si fue exitoso, o null si falló.
     */
    public Ambiente update(int id, Ambiente ambiente) {
        // Consulta SQL para actualizar un ambiente existente
        String SQL = "UPDATE ambientes SET nombre = ?, centro_id = ?, mapa = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, ambiente.getNombre());
            stmt.setInt(2, ambiente.getCentro_id());
            stmt.setString(3, ambiente.getMapa());
            stmt.setInt(4, id); // Establece el ID del ambiente a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto ambiente y lo retorna
                ambiente.setId(id);
                return ambiente;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el ambiente
        return null;
    }

    /**
     * Elimina un ambiente de la base de datos por su ID.
     *
     * @param id ID del ambiente a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un ambiente por ID
        String SQL = "DELETE FROM ambientes WHERE id = ?";

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

    /**
     * Obtiene todos los ambientes registrados en un centro específico.
     *
     * @param id ID del centro.
     * @return Lista de ambientes asociados al centro o vacía si no hay.
     */
    public List<Ambiente> getAllByCentroId(int id) {
        // Inicializa una lista para almacenar los ambientes
        List<Ambiente> ambientes = new ArrayList<>();
        // Consulta SQL para seleccionar ambientes por centro_id
        String SQL = "SELECT * FROM ambientes WHERE centro_id = ? ORDER BY id DESC";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro centro_id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Ambiente a partir de los datos de la fila actual
                Ambiente ambiente = new Ambiente(
                    rs.getInt("id"), // Obtiene el ID del ambiente
                    rs.getString("nombre"), // Obtiene el nombre del ambiente
                    rs.getInt("centro_id"), // Obtiene el ID del centro asociado
                    rs.getString("mapa") // Obtiene el mapa del ambiente
                );
                // Agrega el ambiente a la lista
                ambientes.add(ambiente);
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de ambientes
        return ambientes;
    }
}
