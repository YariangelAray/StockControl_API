package model.dao;

import model.TipoDocumento;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para realizar operaciones CRUD sobre la tabla 'tipos_documento'.
 * 
 * Esta clase se encarga exclusivamente del acceso a datos, sin incluir lógica de negocio.
 * Utiliza la clase DBConnection para conectarse a la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los tipos de documento.
 * - getById(int id): Busca un tipo de documento por su ID.
 * - create(TipoDocumento tipoDocumento): Inserta un nuevo tipo de documento.
 * - update(int id, TipoDocumento tipoDocumento): Actualiza un tipo existente.
 * - delete(int id): Elimina un tipo por su ID.
 * 
 * @author Yariangel Aray
 */
public class TipoDocumentoDAO {

    /**
     * Obtiene todos los registros de tipos de documento desde la base de datos.
     *
     * @return Lista de objetos TipoDocumento con todos los tipos registrados.
     */
    public List<TipoDocumento> getAll() {
        List<TipoDocumento> tipos = new ArrayList<>(); // Lista para almacenar resultados
        String SQL = "SELECT * FROM tipos_documento"; // Consulta SQL

        try (
            Connection conexion = DBConnection.conectar(); // Establece conexión
            PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
            ResultSet rs = stmt.executeQuery() // Ejecuta y obtiene resultados
        ) {
            while (rs.next()) { // Itera sobre cada fila
                TipoDocumento tipo = new TipoDocumento(
                    rs.getInt("id"), // ID del tipo
                    rs.getString("nombre") // Nombre del tipo
                );
                tipos.add(tipo); // Agrega a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime error si ocurre
        }

        return tipos; // Retorna la lista
    }

    /**
     * Busca un tipo de documento por su ID.
     *
     * @param id El ID del tipo de documento a buscar.
     * @return El objeto TipoDocumento si se encuentra, o null si no existe.
     */
    public TipoDocumento getById(int id) {
        TipoDocumento tipo = null; // Inicializa el resultado
        String SQL = "SELECT * FROM tipos_documento WHERE id = ?"; // Consulta con parámetro

        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el ID
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            if (rs.next()) { // Si hay resultado
                tipo = new TipoDocumento(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipo; // Retorna el tipo o null
    }

    /**
     * Inserta un nuevo tipo de documento en la base de datos.
     *
     * @param tipo TipoDocumento con la información a registrar.
     * @return TipoDocumento creado con el ID generado, o null si hubo error.
     */
    public TipoDocumento create(TipoDocumento tipo) {
        String SQL = "INSERT INTO tipos_documento (nombre) VALUES (?)"; // Consulta de inserción

        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, tipo.getNombre()); // Asigna el nombre

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la inserción
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys(); // Obtiene el ID generado
                if (generatedKeys.next()) {
                    tipo.setId(generatedKeys.getInt(1)); // Asigna el ID al objeto
                    return tipo;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si hubo error
    }

    /**
     * Actualiza un tipo de documento existente por su ID.
     *
     * @param id ID del tipo de documento a actualizar.
     * @param tipo Objeto TipoDocumento con los nuevos datos.
     * @return TipoDocumento actualizado si fue exitoso, o null si falló.
     */
    public TipoDocumento update(int id, TipoDocumento tipo) {
        String SQL = "UPDATE tipos_documento SET nombre = ? WHERE id = ?"; // Consulta de actualización

        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setString(1, tipo.getNombre()); // Nuevo nombre
            stmt.setInt(2, id); // ID del registro a actualizar

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la actualización
            if (filasAfectadas > 0) {
                tipo.setId(id); // Actualiza el ID en el objeto
                return tipo;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Elimina un tipo de documento de la base de datos por su ID.
     *
     * @param id ID del tipo de documento a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM tipos_documento WHERE id = ?"; // Consulta de eliminación

        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el ID
            return stmt.executeUpdate() > 0; // Retorna true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }
}

