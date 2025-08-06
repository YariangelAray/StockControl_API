package model.dao;

import model.Elemento;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD
 * sobre la tabla 'elementos' en la base de datos.
 *
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 *
 * Métodos disponibles:
 * - getAll(): Obtiene todos los elementos.
 * - getById(int id): Busca un elemento por su ID.
 * - create(Elemento elemento): Crea un nuevo elemento y retorna el objeto creado.
 * - update(int id, Elemento elemento): Actualiza un elemento por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un elemento por su ID.
 * - getAllByIdInventario(int inventarioId): Obtiene todos los elementos por ID de inventario.
 * - getAllByIdAmbiente(int ambienteId): Obtiene todos los elementos por ID de ambiente.
 * - getAllByIdTipoElemento(int tipoElementoId): Obtiene todos los elementos por ID de tipo de elemento.
 * - getByIdTipoEstado(int estadoId): Obtiene todos los elementos por ID de estado.
 *
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 *
 * @author Yariangel Aray
 */
public class ElementoDAO {

    /**
     * Obtiene todos los registros de elementos desde la base de datos.
     *
     * @return Lista de objetos Elemento con todos los elementos registrados.
     */
    public List<Elemento> getAll() {
        List<Elemento> elementos = new ArrayList<>(); // Lista para almacenar los elementos
        String SQL = "SELECT * FROM elementos ORDER BY id DESC"; // Consulta SQL para obtener todos los elementos

        try (Connection conexion = DBConnection.conectar(); // Establece la conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta SQL
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y guarda los resultados

            while (rs.next()) { // Itera sobre cada fila del resultado
                Elemento elemento = mapearElemento(rs);
                elementos.add(elemento); // Agrega el elemento a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error si ocurre una excepción
        }

        return elementos; // Retorna la lista de elementos
    }

    /**
     * Busca un elemento por su ID.
     *
     * @param id El ID del elemento a buscar.
     * @return El objeto Elemento si se encuentra, o null si no existe.
     */
    public Elemento getById(int id) {
        Elemento elemento = null; // Inicializa el objeto como null
        String SQL = "SELECT * FROM elementos WHERE id = ?"; // Consulta SQL por ID

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, id); // Establece el valor del parámetro ID
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            if (rs.next()) { // Si hay resultado
                elemento = mapearElemento(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error si ocurre
        }

        return elemento; // Retorna el objeto o null
    }

    /**
     * Inserta un nuevo elemento en la base de datos.
     *
     * @param elemento Elemento con la información a registrar.
     * @return Elemento creado con el ID generado, o null si hubo error.
     */
    public Elemento create(Elemento elemento) {
        String SQL = "INSERT INTO elementos (placa, serial, tipo_elemento_id, fecha_adquisicion, valor_monetario, estado_id, observaciones, ambiente_id, inventario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Consulta SQL de inserción

        try (Connection conexion = DBConnection.conectar(); // Establece la conexión
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta con retorno de claves

            stmt.setLong(1, elemento.getPlaca()); // Asigna placa
            stmt.setString(2, elemento.getSerial()); // Asigna serial
            stmt.setInt(3, elemento.getTipo_elemento_id()); // Asigna tipo
            stmt.setDate(4, new java.sql.Date(elemento.getFecha_adquisicion().getTime())); // Asigna fecha
            stmt.setDouble(5, elemento.getValor_monetario()); // Asigna valor
            stmt.setInt(6, elemento.getEstado_id()); // Asigna estado
            stmt.setString(7, elemento.getObservaciones()); // Asigna observaciones  
            
            if (elemento.getAmbiente_id() == 0) { // Asigna ambiente, evaluamos que no sea 0
                stmt.setNull(8, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(8, elemento.getAmbiente_id());
            }                     
            stmt.setInt(9, elemento.getInventario_id()); // Asigna inventario

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la inserción
            if (filasAfectadas > 0) { // Si se insertó correctamente
                ResultSet generatedKeys = stmt.getGeneratedKeys(); // Obtiene el ID generado
                if (generatedKeys.next()) {
                    elemento.setId(generatedKeys.getInt(1)); // Asigna el ID al objeto
                    elemento.setEstado_activo(true); // Asigna el objeto como activo al objeto
                    return elemento; // Retorna el objeto creado
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error
        }

        return null; // Retorna null si hubo error
    }

    /**
     * Actualiza un elemento existente por su ID.
     *
     * @param id ID del elemento a actualizar.
     * @param elemento Objeto Elemento con los nuevos datos.
     * @return true si fue exitoso, o false si falló.
     */
    public boolean update(int id, Elemento elemento) {
        String SQL = "UPDATE elementos SET placa = ?, serial = ?, tipo_elemento_id = ?, fecha_adquisicion = ?, valor_monetario = ?, estado_id = ?, observaciones = ?, ambiente_id = ?, inventario_id = ? WHERE id = ?"; // Consulta SQL de actualización

        try (Connection conexion = DBConnection.conectar(); // Establece la conexión
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setLong(1, elemento.getPlaca()); // Asigna placa
            stmt.setString(2, elemento.getSerial()); // Asigna serial
            stmt.setInt(3, elemento.getTipo_elemento_id()); // Asigna tipo
            stmt.setDate(4, new java.sql.Date(elemento.getFecha_adquisicion().getTime())); // Asigna fecha
            stmt.setDouble(5, elemento.getValor_monetario()); // Asigna valor
            stmt.setInt(6, elemento.getEstado_id()); // Asigna estado
            stmt.setString(7, elemento.getObservaciones()); // Asigna observaciones
            
            if (elemento.getAmbiente_id() == 0) { // Asigna ambiente, evaluamos que no sea 0
                stmt.setNull(8, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(8, elemento.getAmbiente_id());
            }   
            stmt.setInt(9, elemento.getInventario_id()); // Asigna inventario
            stmt.setInt(10, id); // ID del elemento a actualizar

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la actualización

            return filasAfectadas > 0; // Retorna el objeto actualizado

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error
        }

        return false; // Retorna null si falló
    }
    
    /**
    * Actualiza el estado_activo de un elemento por su ID.
    *
    * @param id ID del elemento a modificar.
    * @param estadoActivo Nuevo valor booleano del estado_activo (true o false).
    * @return true si la operación fue exitosa, false si falló.
    */
   public boolean updateState(int id, boolean estadoActivo) {
       String SQL = "UPDATE elementos SET estado_activo = ? WHERE id = ?";

       try (Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)) {

           stmt.setBoolean(1, estadoActivo); // Asigna el nuevo estado
           stmt.setInt(2, id); // ID del elemento

           int filasAfectadas = stmt.executeUpdate(); // Ejecuta la actualización

           return filasAfectadas > 0; // Retorna true si al menos una fila fue modificada

       } catch (SQLException e) {
           e.printStackTrace(); // Muestra error en consola
           return false; // Falló la operación
       }
   }    

    /**
     * Elimina un elemento de la base de datos por su ID.
     *
     * @param id ID del elemento a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM elementos WHERE id = ?"; // Consulta SQL para eliminar

        try (Connection conexion = DBConnection.conectar(); // Establece conexión
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, id); // Asigna el ID
            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la eliminación
            return filasAfectadas > 0; // Retorna true si eliminó

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error
            return false; // Retorna false si falló
        }
    }

    // Métodos adicionales para consultar por campos específicos

    public List<Elemento> getAllByIdInventario(int inventarioId) {
        return getAllByCampo("inventario_id", inventarioId); // Consulta por inventario
    }

    public List<Elemento> getAllByIdAmbiente(int ambienteId) {
        return getAllByCampo("ambiente_id", ambienteId); // Consulta por ambiente
    }

    public List<Elemento> getAllByIdTipoElemento(int tipoElementoId) {
        return getAllByCampo("tipo_elemento_id", tipoElementoId); // Consulta por tipo
    }

    public List<Elemento> getByIdTipoEstado(int estadoId) {
        return getAllByCampo("estado_id", estadoId); // Consulta por estado
    }
    
    public List<Elemento> getBySerial(String serial) {
        return getAllByCampo("serial", serial); // Consulta por tipo
    }

    public List<Elemento> getByPlaca(long placa) {
        return getAllByCampo("placa", placa); // Consulta por estado
    }

    /**
     * Método auxiliar que realiza una consulta genérica por campo y valor.
     *
     * @param campo Nombre del campo a consultar.
     * @param value Valor que debe tener el campo.
     * @return Lista de elementos que cumplen la condición.
     */
    private List<Elemento> getAllByCampo(String campo, int value) {
        List<Elemento> elementos = new ArrayList<>();
        String SQL = "SELECT * FROM elementos WHERE " + campo + " = ? ORDER BY id DESC";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, value); // Usa setInt para enteros
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Elemento elemento = mapearElemento(rs);
                elementos.add(elemento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return elementos;
    }

    private List<Elemento> getAllByCampo(String campo, String value) {
        List<Elemento> elementos = new ArrayList<>();
        String SQL = "SELECT * FROM elementos WHERE " + campo + " = ? ORDER BY id DESC";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, value); // Usa setString para strings
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Elemento elemento = mapearElemento(rs);
                elementos.add(elemento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return elementos;
    }
    
    private List<Elemento> getAllByCampo(String campo, long value) {
        List<Elemento> elementos = new ArrayList<>();
        String SQL = "SELECT * FROM elementos WHERE " + campo + " = ? ORDER BY id DESC";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setLong(1, value); // Usa setString para strings
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Elemento elemento = mapearElemento(rs);
                elementos.add(elemento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return elementos;
    }
    
    
    private Elemento mapearElemento(ResultSet rs) throws SQLException {
        return new Elemento(
            rs.getInt("id"),
            rs.getLong("placa"),
            rs.getString("serial"),
            rs.getInt("tipo_elemento_id"),
            rs.getDate("fecha_adquisicion"),
            rs.getDouble("valor_monetario"),
            rs.getInt("estado_id"),
            rs.getString("observaciones"),
            rs.getBoolean("estado_activo"),
            rs.getInt("ambiente_id"),
            rs.getInt("inventario_id")
        );
    }
}
