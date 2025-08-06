package model.dao;

import model.CodigoAcceso;
import database.DBConnection;

import java.sql.*;

/**
 * DAO para manejar operaciones relacionadas con los códigos de acceso.
 * Sólo se implementan los métodos esenciales para generar y validar códigos.
 * @author Yariangel Aray
 */
public class CodigoAccesoDAO {

    public CodigoAcceso getByIdInventario(int inventarioId){
        String SQL = "SELECT * FROM codigos_acceso WHERE inventario_id = ? ORDER BY fecha_expiracion DESC LIMIT 1";

       try (Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {

           stmt.setInt(1, inventarioId);
           ResultSet rs = stmt.executeQuery();

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

       return null;
    }
    
    /**
     * Inserta un nuevo código de acceso para un inventario.
     */
    public boolean create(CodigoAcceso codigo) {
        String SQL = "INSERT INTO codigos_acceso (codigo, inventario_id, fecha_expiracion) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, codigo.getCodigo());
            stmt.setInt(2, codigo.getInventario_id());            
            stmt.setTimestamp(3, codigo.getFecha_expiracion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca un código de acceso por su valor y verifica que no esté expirado.
     */
    public CodigoAcceso searchValid(String codigoInput) {
        String SQL = "SELECT * FROM codigos_acceso WHERE codigo = ? AND fecha_expiracion > NOW()";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, codigoInput);
            ResultSet rs = stmt.executeQuery();

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

        return null;
    }
    
    /**
    * Verifica si ya existe un código activo para el inventario.
    */
   public CodigoAcceso getCodigoActivo(int inventarioId) {
       String SQL = "SELECT * FROM codigos_acceso WHERE inventario_id = ? AND fecha_expiracion > CURRENT_TIMESTAMP ORDER BY fecha_expiracion DESC LIMIT 1";

       try (Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {

           stmt.setInt(1, inventarioId);
           ResultSet rs = stmt.executeQuery();

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

       return null;
   }
   
   public boolean deleteCodigoPorInventario(int inventarioId) {
        String SQL = "DELETE FROM codigos_acceso WHERE inventario_id = ?";
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
