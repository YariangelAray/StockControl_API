package model.dao; // Paquete donde está ubicado el DAO

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Ficha;
import database.DBConnection;

/**
 * Clase DAO (Data Access Object) que permite realizar operaciones CRUD
 * sobre la tabla 'fichas' en la base de datos.
 * 
 * Esta clase se conecta a la base de datos usando DBConnection y permite:
 * - Listar todas las fichas
 * - Buscar ficha por ID
 * - Buscar fichas por ID de programa
 * - Crear nueva ficha
 * - Actualizar ficha
 * - Eliminar ficha
 * 
 * @author Yariangel
 */
public class FichaDAO {

    /**
     * Obtiene todas las fichas registradas en la base de datos.
     * 
     * @return Lista con todas las fichas.
     */
    public List<Ficha> getAll() {
        List<Ficha> fichas = new ArrayList<>(); // Lista donde se almacenarán las fichas
        String SQL = "SELECT * FROM fichas"; // Consulta SQL para traer todas las fichas

        // Se establece conexión y se ejecuta la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la BD
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Preparamos la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecutamos y obtenemos resultados

            // Iteramos sobre los resultados
            while (rs.next()) {
                // Creamos una nueva ficha con los datos de la fila actual
                Ficha ficha = new Ficha(
                    rs.getInt("id"), // ID de la ficha
                    rs.getString("ficha"), // Número de ficha
                    rs.getInt("programa_id") // ID del programa asociado
                );
                // Agregamos la ficha a la lista
                fichas.add(ficha);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Mostramos error si ocurre
        }

        return fichas; // Devolvemos la lista final
    }

    /**
     * Busca una ficha específica por su ID.
     * 
     * @param id ID de la ficha.
     * @return Ficha encontrada o null si no existe.
     */
    public Ficha getById(int id) {
        Ficha ficha = null; // Inicializamos en null por si no se encuentra
        String SQL = "SELECT * FROM fichas WHERE id = ?"; // Consulta SQL por ID

        try (Connection conexion = DBConnection.conectar(); // Conexión a la BD
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id); // Establecemos el parámetro ID en la consulta
            ResultSet rs = stmt.executeQuery(); // Ejecutamos la consulta

            if (rs.next()) {
                // Si encuentra un resultado, se crea la ficha
                ficha = new Ficha(
                    rs.getInt("id"),
                    rs.getString("ficha"),
                    rs.getInt("programa_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Mostramos error si ocurre
        }

        return ficha; // Retornamos la ficha encontrada o null
    }

    /**
     * Busca todas las fichas que están asociadas a un programa específico.
     * 
     * @param idPrograma ID del programa de formación.
     * @return Lista de fichas asociadas.
     */
    public List<Ficha> getAllByIdPrograma(int idPrograma) {
        List<Ficha> fichas = new ArrayList<>(); // Lista donde se almacenarán los resultados
        String SQL = "SELECT * FROM fichas WHERE programa_id = ?"; // Consulta con filtro por programa

        try (Connection conexion = DBConnection.conectar(); // Conexión a la BD
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, idPrograma); // Establecemos el ID del programa como parámetro
            ResultSet rs = stmt.executeQuery(); // Ejecutamos la consulta

            while (rs.next()) {
                // Creamos la ficha con los datos de la fila actual
                Ficha ficha = new Ficha(
                    rs.getInt("id"),
                    rs.getString("ficha"),
                    rs.getInt("programa_id")
                );
                // La agregamos a la lista
                fichas.add(ficha);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprimimos errores si los hay
        }

        return fichas; // Devolvemos la lista
    }

    /**
     * Inserta una nueva ficha en la base de datos.
     * 
     * @param ficha Objeto Ficha con los datos a registrar.
     * @return Ficha creada con el ID asignado, o null si falla.
     */
    public Ficha create(Ficha ficha) {
        String SQL = "INSERT INTO fichas (ficha, programa_id) VALUES (?, ?)"; // Consulta para insertar

        try (Connection conexion = DBConnection.conectar(); // Conexión a la BD
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ficha.getFicha()); // Establecemos el número de ficha
            stmt.setInt(2, ficha.getPrograma_id()); // Establecemos el ID del programa

            int filasAfectadas = stmt.executeUpdate(); // Ejecutamos la inserción

            if (filasAfectadas > 0) {
                // Si se insertó correctamente, obtenemos la clave generada
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    // Asignamos el ID generado al objeto ficha
                    ficha.setId(keys.getInt(1));
                    return ficha;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Error en consola si algo falla
        }

        return null; // Si algo falla, retornamos null
    }

    /**
     * Actualiza una ficha existente por su ID.
     * 
     * @param id ID de la ficha a actualizar.
     * @param ficha Objeto con los nuevos datos.
     * @return Ficha actualizada o null si falla.
     */
    public Ficha update(int id, Ficha ficha) {
        String SQL = "UPDATE fichas SET ficha = ?, programa_id = ? WHERE id = ?"; // Consulta para actualizar

        try (Connection conexion = DBConnection.conectar(); // Conexión
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, ficha.getFicha()); // Nuevo número de ficha
            stmt.setInt(2, ficha.getPrograma_id()); // Nuevo ID de programa
            stmt.setInt(3, id); // ID de la ficha que se va a actualizar

            int filasAfectadas = stmt.executeUpdate(); // Ejecutamos la actualización

            if (filasAfectadas > 0) {
                ficha.setId(id); // Establecemos el ID actualizado
                return ficha;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Mostramos error si ocurre
        }

        return null; // Si algo falla, retornamos null
    }

    /**
     * Elimina una ficha de la base de datos por su ID.
     * 
     * @param id ID de la ficha a eliminar.
     * @return true si se eliminó correctamente, false si no.
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM fichas WHERE id = ?"; // Consulta para eliminar

        try (Connection conexion = DBConnection.conectar(); // Conexión
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id); // Establecemos el ID como parámetro
            int filasAfectadas = stmt.executeUpdate(); // Ejecutamos

            return filasAfectadas > 0; // Retornamos true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace(); // Mostramos error si ocurre
            return false; // En caso de error, retornamos false
        }
    }
}
