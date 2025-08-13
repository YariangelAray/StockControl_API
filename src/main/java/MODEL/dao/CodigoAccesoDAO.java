package model.dao;

import model.CodigoAcceso;
import database.DBConnection;

import java.sql.*;

/**
 * DAO para manejar operaciones relacionadas con los códigos de acceso.
 * 
 * Esta clase permite:
 * - Obtener el último código por inventario
 * - Crear un nuevo código
 * - Validar si un código está activo
 * - Verificar si existe un código activo para un inventario
 * - Eliminar códigos por inventario
 * 
 * No contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class CodigoAccesoDAO {

    /**
     * Obtiene el último código de acceso registrado para un inventario específico.
     * 
     * @param inventarioId ID del inventario
     * @return Último CódigoAcceso o null si no existe
     */
    public CodigoAcceso getByIdInventario(int inventarioId) {
        String SQL = "SELECT * FROM codigos_acceso WHERE inventario_id = ? ORDER BY fecha_expiracion DESC LIMIT 1";

        // Bloque para ejecutar la consulta y obtener el código más reciente
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, inventarioId); // Asigna el ID del inventario
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Verifica si hay resultado
            if (rs.next()) {
                return new CodigoAcceso(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getInt("inventario_id"),
                    rs.getTimestamp("fecha_expiracion")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no se encuentra
    }

    /**
     * Inserta un nuevo código de acceso para un inventario.
     * 
     * @param codigo Objeto CodigoAcceso a insertar
     * @return true si fue insertado correctamente, false si falló
     */
    public boolean create(CodigoAcceso codigo) {
        String SQL = "INSERT INTO codigos_acceso (codigo, inventario_id, fecha_expiracion) VALUES (?, ?, ?)";

        // Bloque para ejecutar la inserción
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, codigo.getCodigo()); // Asigna el valor del código
            stmt.setInt(2, codigo.getInventario_id()); // Asigna el ID del inventario
            stmt.setTimestamp(3, codigo.getFecha_expiracion()); // Asigna la fecha de expiración

            return stmt.executeUpdate() > 0; // Retorna true si se insertó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }

    /**
     * Busca un código de acceso por su valor y verifica que no esté expirado.
     * 
     * @param codigoInput Código a validar
     * @return Objeto CodigoAcceso si es válido, o null si está expirado o no existe
     */
    public CodigoAcceso searchValid(String codigoInput) {
        String SQL = "SELECT * FROM codigos_acceso WHERE codigo = ? AND fecha_expiracion > NOW()";

        // Bloque para buscar el código y validar su vigencia
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, codigoInput); // Asigna el código a buscar
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Verifica si hay resultado válido
            if (rs.next()) {
                return new CodigoAcceso(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getInt("inventario_id"),
                    rs.getTimestamp("fecha_expiracion")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no es válido
    }

    /**
     * Verifica si ya existe un código activo para el inventario.
     * 
     * @param inventarioId ID del inventario
     * @return CódigoAcceso activo más reciente o null si no hay
     */
    public CodigoAcceso getCodigoActivo(int inventarioId) {
        String SQL = "SELECT * FROM codigos_acceso WHERE inventario_id = ? AND fecha_expiracion > CURRENT_TIMESTAMP ORDER BY fecha_expiracion DESC LIMIT 1";

        // Bloque para buscar el código activo más reciente
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, inventarioId); // Asigna el ID del inventario
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Verifica si hay resultado
            if (rs.next()) {
                return new CodigoAcceso(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getInt("inventario_id"),
                    rs.getTimestamp("fecha_expiracion")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no hay código activo
    }

    /**
     * Elimina todos los códigos de acceso asociados a un inventario.
     * 
     * @param inventarioId ID del inventario
     * @return true si se eliminaron registros, false si no
     */
    public boolean deleteCodigoPorInventario(int inventarioId) {
        String SQL = "DELETE FROM codigos_acceso WHERE inventario_id = ?";

        // Bloque para ejecutar la eliminación
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, inventarioId); // Asigna el ID del inventario
            return stmt.executeUpdate() > 0; // Retorna true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }
}