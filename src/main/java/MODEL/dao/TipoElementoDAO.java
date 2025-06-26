package model.dao;

import model.entity.TipoElemento;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'tipos_elementos' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los tipos de elementos.
 * - getById(int id): Busca un tipo de elemento por su ID.
 * - create(TipoElemento tipoElemento): Crea un nuevo tipo de elemento y retorna el objeto creado.
 * - update(int id, TipoElemento tipoElemento): Actualiza un tipo de elemento por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un tipo de elemento por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class TipoElementoDAO {

    /**
     * Obtiene todos los registros de tipos de elementos desde la base de datos.
     *
     * @return Lista de objetos TipoElemento con todos los tipos registrados.
     */
    public List<TipoElemento> getAll() {
        // Inicializa una lista para almacenar los tipos
        List<TipoElemento> tipos = new ArrayList<>();
        // Consulta SQL para seleccionar todos los tipos de elementos
        String SQL = "SELECT * FROM tipos_elementos";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto TipoElemento a partir de los datos de la fila actual
                TipoElemento tipo = new TipoElemento(
                    rs.getInt("id"), // Obtiene el ID
                    rs.getString("nombre"), // Obtiene el nombre
                    rs.getString("descripcion"), // Obtiene la descripción
                    rs.getString("marca"), // Obtiene la marca
                    rs.getString("modelo"), // Obtiene el modelo
                    rs.getString("observaciones") // Obtiene las observaciones
                );
                // Agrega el tipo a la lista
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de tipos
        return tipos;
    }

    /**
     * Busca un tipo de elemento por su ID.
     *
     * @param id El ID del tipo a buscar.
     * @return El objeto TipoElemento si se encuentra, o null si no existe.
     */
    public TipoElemento getById(int id) {
        // Inicializa el objeto como null
        TipoElemento tipo = null;
        // Consulta SQL para seleccionar un tipo por ID
        String SQL = "SELECT * FROM tipos_elementos WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto TipoElemento a partir de los datos de la fila actual
                tipo = new TipoElemento(
                    rs.getInt("id"), // Obtiene el ID
                    rs.getString("nombre"), // Obtiene el nombre
                    rs.getString("descripcion"), // Obtiene la descripción
                    rs.getString("marca"), // Obtiene la marca
                    rs.getString("modelo"), // Obtiene el modelo
                    rs.getString("observaciones") // Obtiene las observaciones
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el tipo encontrado o null si no existe
        return tipo;
    }

    /**
     * Inserta un nuevo tipo de elemento en la base de datos.
     *
     * @param tipo TipoElemento con la información a registrar.
     * @return TipoElemento creado con el ID generado, o null si hubo error.
     */
    public TipoElemento create(TipoElemento tipo) {
        // Consulta SQL para insertar un nuevo tipo
        String SQL = "INSERT INTO tipos_elementos (nombre, descripcion, marca, modelo, observaciones) VALUES (?, ?, ?, ?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());
            stmt.setString(3, tipo.getMarca());
            stmt.setString(4, tipo.getModelo());
            stmt.setString(5, tipo.getObservaciones());

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo tipo)
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
        // Retorna null si hubo un error al crear el tipo
        return null;
    }

    /**
     * Actualiza un tipo de elemento existente por su ID.
     *
     * @param id ID del tipo a actualizar.
     * @param tipo Objeto TipoElemento con los nuevos datos.
     * @return TipoElemento actualizado si fue exitoso, o null si falló.
     */
    public TipoElemento update(int id, TipoElemento tipo) {
        // Consulta SQL para actualizar un tipo existente
        String SQL = "UPDATE tipos_elementos SET nombre = ?, descripcion = ?, marca = ?, modelo = ?, observaciones = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());
            stmt.setString(3, tipo.getMarca());
            stmt.setString(4, tipo.getModelo());
            stmt.setString(5, tipo.getObservaciones());
            stmt.setInt(6, id); // Establece el ID del tipo a actualizar

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
        // Retorna null si hubo un error al actualizar el tipo
        return null;
    }

    /**
     * Elimina un tipo de elemento de la base de datos por su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un tipo por ID
        String SQL = "DELETE FROM tipos_elementos WHERE id = ?";

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
