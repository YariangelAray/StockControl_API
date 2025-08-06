package model.dao;

import model.Reporte;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD
 * sobre la tabla 'reportes' en la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los reportes.
 * - getById(int id): Busca un reporte por su ID.
 * - create(Reporte reporte): Crea un nuevo reporte y retorna el objeto creado.
 * - update(int id, Reporte reporte): Actualiza un reporte por su ID.
 * - delete(int id): Elimina un reporte por su ID.
 * - getAllByIdElemento(int idElemento): Obtiene todos los reportes asociados a un elemento.
 * 
 * @author Yariangel
 */
public class ReporteDAO {

    /**
     * Obtiene todos los reportes registrados en la base de datos.
     *
     * @return Lista de objetos Reporte con todos los registros.
     */
    public List<Reporte> getAll() {
        List<Reporte> reportes = new ArrayList<>(); // Lista que almacenará los reportes encontrados
        String SQL = "SELECT * FROM reportes ORDER BY id DESC"; // Consulta SQL para obtener todos los reportes

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y almacena el resultado

            // Recorre el resultado fila por fila
            while (rs.next()) {
                // Crea un nuevo objeto Reporte con los datos obtenidos
                Reporte reporte = new Reporte(
                        rs.getInt("id"), // ID del reporte
                        rs.getDate("fecha"), // Fecha del reporte
                        rs.getString("asunto"), // Asunto del reporte
                        rs.getString("mensaje"), // Mensaje del reporte
                        rs.getInt("usuario_id"), // ID del usuario que reporta
                        rs.getInt("elemento_id") // ID del elemento asociado
                );
                reportes.add(reporte); // Agrega el reporte a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en consola si ocurre
        }
        return reportes; // Retorna la lista de reportes
    }
    
    /**
     * Obtiene todos los reportes asociados a un inventario específico.
     * 
     * @param inventarioId ID del inventario del cual se desean obtener los reportes.
     * @return Lista de objetos Reporte relacionados a los elementos de ese inventario.
     */
    public List<Reporte> getAllByIdInventario(int inventarioId) {
        List<Reporte> reportes = new ArrayList<>(); // Lista para almacenar los reportes encontrados

        // Consulta SQL que une la tabla reportes con elementos
        // y filtra los elementos que pertenecen al inventario dado
        String SQL = "SELECT r.* FROM reportes r JOIN elementos e ON r.elemento_id = e.id WHERE e.inventario_id = ? ORDER BY id DESC";

        try (Connection conexion = DBConnection.conectar(); // Abre conexión a la base de datos
            PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta SQL        
            stmt.setInt(1, inventarioId); // Asigna el ID del inventario como parámetro

            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta y obtiene los resultados

            // Recorre cada fila del resultado
            while (rs.next()) {
                // Crea un nuevo objeto Reporte con los datos obtenidos
                Reporte reporte = new Reporte(
                    rs.getInt("id"),            // ID del reporte
                    rs.getDate("fecha"),        // Fecha del reporte
                    rs.getString("asunto"),     // Asunto del reporte
                    rs.getString("mensaje"),    // Mensaje detallado
                    rs.getInt("usuario_id"),    // ID del usuario que lo creó
                    rs.getInt("elemento_id")    // ID del elemento relacionado
                );
                reportes.add(reporte); // Agrega el reporte a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error si ocurre una excepción
        }

        return reportes; // Devuelve la lista de reportes encontrados
    }


    /**
     * Busca un reporte por su ID.
     *
     * @param id ID del reporte a buscar.
     * @return Objeto Reporte si se encuentra, o null si no existe.
     */
    public Reporte getById(int id) {
        Reporte reporte = null; // Objeto que almacenará el resultado
        String SQL = "SELECT * FROM reportes WHERE id = ?"; // Consulta SQL

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, id); // Asigna el ID como parámetro a la consulta
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            if (rs.next()) { // Si se encontró un resultado
                reporte = new Reporte(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getString("asunto"),
                        rs.getString("mensaje"),
                        rs.getInt("usuario_id"),
                        rs.getInt("elemento_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reporte; // Retorna el reporte encontrado o null
    }

    /**
     * Inserta un nuevo reporte en la base de datos.
     *
     * @param reporte Objeto con los datos del nuevo reporte.
     * @return Reporte creado con el ID generado, o null si hubo error.
     */
    public Reporte create(Reporte reporte) {
        String SQL = "INSERT INTO reportes (asunto, mensaje, usuario_id, elemento_id) VALUES (?, ?, ?, ?)"; // Consulta SQL

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener claves generadas

            // Asigna los valores del reporte a los parámetros
            stmt.setString(1, reporte.getAsunto());
            stmt.setString(2, reporte.getMensaje());
            stmt.setInt(3, reporte.getUsuario_id());
            stmt.setInt(4, reporte.getElemento_id());

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la inserción
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys(); // Obtiene el ID generado
                if (generatedKeys.next()) {
                    reporte.setId(generatedKeys.getInt(1)); // Asigna el ID al objeto
                    return reporte; // Retorna el reporte con ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si hubo error
    }

    /**
     * Actualiza un reporte existente por su ID.
     *
     * @param id ID del reporte a actualizar.
     * @param reporte Objeto Reporte con los nuevos datos.
     * @return Reporte actualizado si fue exitoso, o null si falló.
     */
    public Reporte update(int id, Reporte reporte) {
        String SQL = "UPDATE reportes SET asunto = ?, mensaje = ?, usuario_id = ?, elemento_id = ? WHERE id = ?"; // Consulta SQL de actualización

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setString(1, reporte.getAsunto()); // Asigna el asunto
            stmt.setString(2, reporte.getMensaje()); // Asigna el mensaje
            stmt.setInt(3, reporte.getUsuario_id()); // Asigna el ID del usuario
            stmt.setInt(4, reporte.getElemento_id()); // Asigna el ID del elemento
            stmt.setInt(5, id); // Asigna el ID del reporte a actualizar

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la actualización
            if (filasAfectadas > 0) {
                reporte.setId(id); // Asigna el ID al objeto reporte
                return reporte; // Retorna el objeto actualizado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si hubo error
    }

    /**
     * Elimina un reporte de la base de datos por su ID.
     *
     * @param id ID del reporte a eliminar.
     * @return true si fue eliminado correctamente, false si hubo error.
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM reportes WHERE id = ?"; // Consulta SQL para eliminar

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, id); // Asigna el ID a eliminar
            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la eliminación
            return filasAfectadas > 0; // Retorna true si se eliminó
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }

    /**
     * Obtiene todos los reportes relacionados con un elemento específico.
     *
     * @param idElemento ID del elemento del cual se desean obtener los reportes.
     * @return Lista de reportes relacionados al elemento.
     */
    public List<Reporte> getAllByIdElemento(int idElemento) {
        List<Reporte> reportes = new ArrayList<>(); // Lista para almacenar los reportes encontrados
        String SQL = "SELECT * FROM reportes WHERE elemento_id = ? ORDER BY id DESC"; // Consulta SQL con filtro por elemento_id

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, idElemento); // Asigna el ID del elemento como parámetro
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Itera sobre los resultados
            while (rs.next()) {
                Reporte reporte = new Reporte(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getString("asunto"),
                        rs.getString("mensaje"),
                        rs.getInt("usuario_id"),
                        rs.getInt("elemento_id")
                );
                reportes.add(reporte); // Agrega cada reporte a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime error en consola
        }
        return reportes; // Retorna la lista de reportes encontrados
    }
}
