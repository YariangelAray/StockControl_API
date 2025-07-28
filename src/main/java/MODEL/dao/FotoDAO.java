package model.dao;

import model.entity.Foto;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para la entidad Foto. Realiza operaciones CRUD sobre la tabla 'fotos'.
 * 
 * Métodos incluidos:
 * - getAll(): obtener todas las fotos
 * - getById(int): obtener una foto específica
 * - getAllByIdReporte(int): obtener fotos de un reporte
 * - create(Foto): guardar nueva foto
 * - delete(int): eliminar una foto por ID
 * 
 * @author
 */
public class FotoDAO {

    /**
     * Obtiene todas las fotos registradas.
     * @return Lista de objetos Foto.
     */
    public List<Foto> getAll() {
        List<Foto> fotos = new ArrayList<>();
        String SQL = "SELECT * FROM fotos";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Foto foto = new Foto(
                    rs.getInt("id"),
                    rs.getString("url"),
                    rs.getInt("reporte_id")
                );
                fotos.add(foto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fotos;
    }

    /**
     * Obtiene una foto específica por su ID.
     * @param id ID de la foto
     * @return Objeto Foto o null si no se encuentra
     */
    public Foto getById(int id) {
        Foto foto = null;
        String SQL = "SELECT * FROM fotos WHERE id = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                foto = new Foto(
                    rs.getInt("id"),
                    rs.getString("url"),
                    rs.getInt("reporte_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foto;
    }

    /**
     * Obtiene todas las fotos relacionadas con un reporte específico.
     * @param reporteId ID del reporte
     * @return Lista de fotos asociadas a ese reporte
     */
    public List<Foto> getAllByIdReporte(int reporteId) {
        List<Foto> fotos = new ArrayList<>();
        String SQL = "SELECT * FROM fotos WHERE reporte_id = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, reporteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Foto foto = new Foto(
                    rs.getInt("id"),
                    rs.getString("url"),
                    rs.getInt("reporte_id")
                );
                fotos.add(foto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fotos;
    }

    /**
     * Inserta una nueva foto en la base de datos.
     * @param foto Objeto Foto a insertar
     * @return Foto creada con ID generado, o null si falla
     */
    public Foto create(Foto foto) {
        String SQL = "INSERT INTO fotos (url, reporte_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, foto.getUrl());
            stmt.setInt(2, foto.getReporte_id());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    foto.setId(generatedKeys.getInt(1));
                    return foto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Elimina una foto por su ID.
     * @param id ID de la foto a eliminar
     * @return true si fue eliminada, false si no
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM fotos WHERE id = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
