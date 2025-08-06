package model.dao;

import model.AccesoTemporal;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CodigoAcceso;

/**
 *
 * @author YariangelAray
 */
public class AccesoTemporalDAO {
    
    public List<AccesoTemporal> getAccesosPorCodigo(int codigoId) {
        List<AccesoTemporal> accesos = new ArrayList<>();
        String SQL = "SELECT * FROM accesos_temporales WHERE codigo_acceso_id = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, codigoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AccesoTemporal acceso = new AccesoTemporal(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getInt("codigo_acceso_id")
                );
                accesos.add(acceso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accesos;
    }


    /**
     * Registra un nuevo acceso temporal para un usuario a un inventario.
     */
    public boolean createAcceso(AccesoTemporal acceso) {
        String SQL = "INSERT INTO accesos_temporales (usuario_id, codigo_acceso_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, acceso.getUsuario_id());
            stmt.setInt(2, acceso.getCodigo_acceso_id());            

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<CodigoAcceso> getCodigosAccesoActivosPorUsuario(int usuarioId) {
        List<CodigoAcceso> codigos = new ArrayList<>();
        String SQL = """
            SELECT ca.* FROM accesos_temporales at
            JOIN codigos_acceso ca ON at.codigo_acceso_id = ca.id
            WHERE at.usuario_id = ? AND ca.fecha_expiracion > CURRENT_TIMESTAMP""";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CodigoAcceso codigo = new CodigoAcceso(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getInt("inventario_id"),
                    rs.getTimestamp("fecha_expiracion")
                );
                codigos.add(codigo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codigos;
    }


    public boolean deleteAccesosPorCodigo(int codigoId) {
        String SQL = "DELETE FROM accesos_temporales WHERE codigo_acceso_id = ?";
        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, codigoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

