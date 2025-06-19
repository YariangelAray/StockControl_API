package model.dao;

import model.entity.TipoDocumento;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'tipos_documento' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los tipos de documento.
 * - getById(int id): Busca un tipo de documento por su ID.
 * - create(TipoDocumento tipoDocumento): Crea un nuevo tipo de documento y retorna el objeto creado.
 * - update(int id, TipoDocumento tipoDocumento): Actualiza un tipo de documento por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un tipo de documento por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class TipoDocumentoDAO {

    /**
     * Obtiene todos los registros de tipos de documento desde la base de datos.
     *
     * @return Lista de objetos TipoDocumento con todos los tipos registrados.
     */
    public List<TipoDocumento> getAll() {
        // Inicializa una lista para almacenar los tipos de documento
        List<TipoDocumento> tipos = new ArrayList<>();
        // Consulta SQL para seleccionar todos los tipos de documento
        String SQL = "SELECT * FROM tipos_documento";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto TipoDocumento a partir de los datos de la fila actual
                TipoDocumento tipo = new TipoDocumento(
                    rs.getInt("id"), // Obtiene el ID del tipo de documento
                    rs.getString("nombre") // Obtiene el nombre del tipo de documento
                );
                // Agrega el tipo de documento a la lista
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de tipos de documento
        return tipos;
    }

    /**
     * Busca un tipo de documento por su ID.
     *
     * @param id El ID del tipo de documento a buscar.
     * @return El objeto TipoDocumento si se encuentra, o null si no existe.
     */
    public TipoDocumento getById(int id) {
        // Inicializa el objeto TipoDocumento como null
        TipoDocumento tipo = null;
        // Consulta SQL para seleccionar un tipo de documento por ID
        String SQL = "SELECT * FROM tipos_documento WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto TipoDocumento a partir de los datos de la fila actual
                tipo = new TipoDocumento(
                    rs.getInt("id"), // Obtiene el ID del tipo de documento
                    rs.getString("nombre") // Obtiene el nombre del tipo de documento
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el tipo de documento encontrado o null si no existe
        return tipo;
    }

    /**
     * Inserta un nuevo tipo de documento en la base de datos.
     *
     * @param tipo TipoDocumento con la información a registrar.
     * @return TipoDocumento creado con el ID generado, o null si hubo error.
     */
    public TipoDocumento create(TipoDocumento tipo) {
        // Consulta SQL para insertar un nuevo tipo de documento
        String SQL = "INSERT INTO tipos_documento (nombre) VALUES (?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, tipo.getNombre());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo tipo de documento)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto tipo y lo retorna
                    tipo.setId(generatedKeys.getInt(1));
                    return tipo;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al crear el tipo de documento
        return null;
    }

    /**
     * Actualiza un tipo de documento existente por su ID.
     *
     * @param id ID del tipo de documento a actualizar.
     * @param tipo Objeto TipoDocumento con los nuevos datos.
     * @return TipoDocumento actualizado si fue exitoso, o null si falló.
     */
    public TipoDocumento update(int id, TipoDocumento tipo) {
        // Consulta SQL para actualizar un tipo de documento existente
        String SQL = "UPDATE tipos_documento SET nombre = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, tipo.getNombre());
            stmt.setInt(2, id); // Establece el ID del tipo de documento a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto tipo y lo retorna
                tipo.setId(id);
                return tipo;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el tipo de documento
        return null;
    }

    /**
     * Elimina un tipo de documento de la base de datos por su ID.
     *
     * @param id ID del tipo de documento a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un tipo de documento por ID
        String SQL = "DELETE FROM tipos_documento WHERE id = ?";

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
