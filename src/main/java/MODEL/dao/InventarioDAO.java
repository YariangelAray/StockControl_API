package model.dao;

import model.entity.Inventario;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.dto.AmbienteDTO;

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
        String SQL = "SELECT * FROM inventarios ORDER BY id DESC";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Inventario a partir de los datos de la fila actual
                Inventario inventario = mapearInventario(rs);
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
     * Obtiene todos los inventarios que tiene un administrador
     *
     * @param idUsuarioAdmin ID del usuario
     * @return Lista de inventarios o vacío si no hay coincidencias.
     */
    public List<Inventario> getAllByIdUserAdmin(int idUsuarioAdmin) {
        return getAllByCampo("usuario_admin_id", idUsuarioAdmin); // Consulta por tipo de documento
    }
    
    /**
     * Obtiene todos los ambientes que esten cubiertos por un inventario
     *
     * @param inventarioId ID del inventario
     * @return Lista de ambientes o vacío si no hay coincidencias.
     */
    public List<AmbienteDTO> getAllAmbientesByInventario(int inventarioId) {
        List<AmbienteDTO> ambientes = new ArrayList<>();

        String SQL = """ 
            SELECT a.id AS ambiente_id, a.nombre AS ambiente_nombre, COUNT(e.id) AS cantidad_elementos
            FROM elementos e JOIN ambientes a ON e.ambiente_id = a.id WHERE e.inventario_id = ? GROUP BY a.id, a.nombre ORDER BY a.nombre""";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, inventarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AmbienteDTO ambiente = new AmbienteDTO(
                    rs.getInt("ambiente_id"),
                    rs.getString("ambiente_nombre"),
                    rs.getInt("cantidad_elementos")
                );
                ambientes.add(ambiente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ambientes;
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
                inventario = mapearInventario(rs);
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
            stmt.setDate(2, new java.sql.Date(inventario.getFecha_creacion().getTime())); // Asigna fecha
            stmt.setInt(3, inventario.getUsuario_admin_id());
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
    
    /**
    * Método auxiliar que realiza una consulta genérica por campo y valor para la tabla de inventarios.
    *
    * Este método es útil para obtener inventarios filtrados por campos específicos como `usuario_admin_id`.
    *
    * @param campo Nombre del campo por el cual se desea filtrar (por ejemplo, "usuario_admin_id").
    * @param value Valor que debe tener el campo especificado.
    * @return Lista de objetos Inventario que cumplen con el criterio, o una lista vacía si no hay coincidencias.
    */
   private List<Inventario> getAllByCampo(String campo, int value) {
       // Inicializa una lista para almacenar los resultados encontrados
       List<Inventario> inventarios = new ArrayList<>();

       // Arma dinámicamente la consulta SQL con el campo recibido
       String SQL = "SELECT * FROM inventarios WHERE " + campo + " = ? ORDER BY id DESC";

       // Intenta establecer una conexión y ejecutar la consulta
       try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
            PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta con parámetros

           // Asigna el valor recibido como parámetro para la condición WHERE
           stmt.setInt(1, value);

           // Ejecuta la consulta y guarda el resultado en un ResultSet
           ResultSet rs = stmt.executeQuery();

           // Itera sobre los resultados obtenidos
           while (rs.next()) {
               // Crea un nuevo objeto Inventario con los datos de la fila actual
               Inventario inventario = mapearInventario(rs);

               // Agrega el inventario a la lista de resultados
               inventarios.add(inventario);
           }

       } catch (SQLException e) {
           // Imprime el error en caso de fallo en la consulta
           e.printStackTrace();
       }

       // Retorna la lista de inventarios encontrados (puede estar vacía)
       return inventarios;
   }
   
   private Inventario mapearInventario (ResultSet rs) throws SQLException {
       return new Inventario(
            rs.getInt("id"), // Obtiene el ID del inventario
            rs.getString("nombre"), // Obtiene el nombre del inventario
            rs.getDate("fecha_creacion"), // Obtiene la fecha de creación del inventario
            rs.getDate("ultima_actualizacion"), // Obtiene la fecha de actualización del inventario
            rs.getInt("usuario_admin_id") // Obtiene el ID del usuario administrador
        );
   }
}
