package model.dao;

import model.Foto;
import database.DBConnection;

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
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class FotoDAO {

    /**
     * Obtiene todas las fotos registradas en la base de datos.
     *
     * @return Lista de objetos Foto.
     */
    public List<Foto> getAll() {
        List<Foto> fotos = new ArrayList<>(); // Lista para almacenar resultados
        String SQL = "SELECT * FROM fotos"; // Consulta SQL

        // Bloque para ejecutar la consulta y recorrer los resultados
        try (
            Connection conn = DBConnection.conectar(); // Establece conexión
            PreparedStatement stmt = conn.prepareStatement(SQL); // Prepara la consulta
            ResultSet rs = stmt.executeQuery() // Ejecuta la consulta
        ) {
            // Iteración sobre cada fila del resultado
            while (rs.next()) {
                Foto foto = new Foto(
                    rs.getInt("id"),
                    rs.getString("url"),
                    rs.getInt("reporte_id")
                );
                fotos.add(foto); // Agrega la foto a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime error si ocurre
        }

        return fotos; // Retorna la lista de fotos
    }

    /**
     * Obtiene una foto específica por su ID.
     *
     * @param id ID de la foto
     * @return Objeto Foto o null si no se encuentra
     */
    public Foto getById(int id) {
        Foto foto = null; // Inicializa el resultado
        String SQL = "SELECT * FROM fotos WHERE id = ?"; // Consulta con parámetro

        // Bloque para ejecutar la consulta con ID
        try (
            Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el ID
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Verifica si hay resultado
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

        return foto; // Retorna la foto o null
    }

    /**
     * Obtiene todas las fotos relacionadas con un reporte específico.
     *
     * @param reporteId ID del reporte
     * @return Lista de fotos asociadas a ese reporte
     */
    public List<Foto> getAllByIdReporte(int reporteId) {
        List<Foto> fotos = new ArrayList<>(); // Lista para almacenar resultados
        String SQL = "SELECT * FROM fotos WHERE reporte_id = ?"; // Consulta con filtro por reporte

        // Bloque para ejecutar la consulta con parámetro reporte_id
        try (
            Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, reporteId); // Asigna el ID del reporte
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Iteración sobre los resultados obtenidos
            while (rs.next()) {
                Foto foto = new Foto(
                    rs.getInt("id"),
                    rs.getString("url"),
                    rs.getInt("reporte_id")
                );
                fotos.add(foto); // Agrega la foto a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fotos; // Retorna la lista de fotos
    }

    /**
     * Inserta una nueva foto en la base de datos.
     *
     * @param foto Objeto Foto a insertar
     * @return Foto creada con ID generado, o null si falla
     */
    public Foto create(Foto foto) {
        String SQL = "INSERT INTO fotos (url, reporte_id) VALUES (?, ?)"; // Consulta de inserción

        // Bloque para ejecutar la inserción y recuperar el ID generado
        try (
            Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, foto.getUrl()); // Asigna la URL
            stmt.setInt(2, foto.getReporte_id()); // Asigna el ID del reporte

            int filas = stmt.executeUpdate(); // Ejecuta la inserción

            // Verifica si se insertó correctamente
            if (filas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys(); // Obtiene el ID generado
                if (generatedKeys.next()) {
                    foto.setId(generatedKeys.getInt(1)); // Asigna el ID al objeto
                    return foto; // Retorna la foto creada
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si hubo error
    }

    /**
     * Elimina una foto por su ID.
     *
     * @param id ID de la foto a eliminar
     * @return true si fue eliminada, false si no
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM fotos WHERE id = ?"; // Consulta de eliminación

        // Bloque para ejecutar la eliminación
        try (
            Connection conn = DBConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el ID
            int filas = stmt.executeUpdate(); // Ejecuta la eliminación
            return filas > 0; // Retorna true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Retorna false si hubo error
    }
}