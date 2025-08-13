package model.dao;

import model.AccesoTemporal;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CodigoAcceso;

/**
 * DAO para manejar accesos temporales de usuarios a inventarios mediante códigos de acceso.
 * 
 * Funcionalidades:
 * - Obtener accesos por código
 * - Registrar nuevos accesos
 * - Consultar códigos activos por usuario
 * - Eliminar accesos por código
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class AccesoTemporalDAO {

    /**
     * Obtiene todos los accesos temporales asociados a un código de acceso.
     *
     * @param codigoId ID del código de acceso
     * @return Lista de objetos AccesoTemporal
     */
    public List<AccesoTemporal> getAccesosPorCodigo(int codigoId) {
        List<AccesoTemporal> accesos = new ArrayList<>(); // Lista para almacenar resultados
        String SQL = "SELECT * FROM accesos_temporales WHERE codigo_acceso_id = ?"; // Consulta SQL

        // Bloque para ejecutar la consulta y recorrer los resultados
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, codigoId); // Asigna el ID del código
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Iteración sobre cada fila del resultado
            while (rs.next()) {
                AccesoTemporal acceso = new AccesoTemporal(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getInt("codigo_acceso_id")
                );
                accesos.add(acceso); // Agrega el acceso a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accesos; // Retorna la lista de accesos
    }

    /**
     * Registra un nuevo acceso temporal para un usuario a un inventario.
     *
     * @param acceso Objeto AccesoTemporal a insertar
     * @return true si se insertó correctamente, false si falló
     */
    public boolean createAcceso(AccesoTemporal acceso) {
        String SQL = "INSERT INTO accesos_temporales (usuario_id, codigo_acceso_id) VALUES (?, ?)";

        // Bloque para ejecutar la inserción
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, acceso.getUsuario_id()); // Asigna el ID del usuario
            stmt.setInt(2, acceso.getCodigo_acceso_id()); // Asigna el ID del código

            return stmt.executeUpdate() > 0; // Retorna true si se insertó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }

    /**
     * Obtiene todos los códigos de acceso activos que tiene asignado un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de objetos CodigoAcceso activos
     */
    public List<CodigoAcceso> getCodigosAccesoActivosPorUsuario(int usuarioId) {
        List<CodigoAcceso> codigos = new ArrayList<>(); // Lista para almacenar resultados

        // Consulta SQL con JOIN entre accesos y códigos, filtrando por usuario y expiración
        String SQL = """
            SELECT ca.* FROM accesos_temporales at
            JOIN codigos_acceso ca ON at.codigo_acceso_id = ca.id
            WHERE at.usuario_id = ? AND ca.fecha_expiracion > CURRENT_TIMESTAMP""";

        // Bloque para ejecutar la consulta y recorrer los resultados
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, usuarioId); // Asigna el ID del usuario
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Iteración sobre cada fila del resultado
            while (rs.next()) {
                CodigoAcceso codigo = new CodigoAcceso(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getInt("inventario_id"),
                    rs.getTimestamp("fecha_expiracion")
                );
                codigos.add(codigo); // Agrega el código a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codigos; // Retorna la lista de códigos activos
    }

    /**
     * Elimina todos los accesos temporales asociados a un código de acceso.
     * 
     * @param codigoId ID del código de acceso
     * @return true si se eliminaron registros, false si no
     */
    public boolean deleteAccesosPorCodigo(int codigoId) {
        String SQL = "DELETE FROM accesos_temporales WHERE codigo_acceso_id = ?";

        // Bloque para ejecutar la eliminación
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, codigoId); // Asigna el ID del código
            return stmt.executeUpdate() > 0; // Retorna true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }
}
