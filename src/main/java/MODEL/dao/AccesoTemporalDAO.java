package model.dao;

import model.entity.AccesoTemporal;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YariangelAray
 */
public class AccesoTemporalDAO {

    /**
     * Registra un nuevo acceso temporal para un usuario a un inventario.
     */
    public boolean createAcceso(AccesoTemporal acceso) {
        String SQL = "INSERT INTO accesos_temporales (usuario_id, inventario_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, acceso.getUsuario_id());
            stmt.setInt(2, acceso.getInventario_id());            

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Integer> getInventariosAccesoUsuario(int usuarioId) {
        List<Integer> inventarios = new ArrayList<>();
        String SQL = """
            SELECT at.inventario_id FROM accesos_temporales at JOIN codigos_acceso ca ON at.inventario_id = ca.inventario_id
            WHERE at.usuario_id = ? AND ca.fecha_expiracion > CURRENT_TIMESTAMP""";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventarios.add(rs.getInt("inventario_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventarios;
    }

    public boolean deleteAccesosInventario(int inventarioId) {
        String SQL = "DELETE FROM accesos_temporales WHERE inventario_id = ?";
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, inventarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

