package model.dao;

import model.TipoElemento;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD
 * sobre la tabla 'tipos_elementos' en la base de datos.
 *
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 *
 * Métodos disponibles:
 * - getAll(): Obtiene todos los tipos de elementos.
 * - getById(int id): Busca un tipo de elemento por su ID.
 * - getByConsecutivo(int consecutivo): Busca un tipo de elemento por su consecutivo (único).
 * - create(TipoElemento tipoElemento): Crea un nuevo tipo de elemento y retorna el objeto creado.
 * - update(int id, TipoElemento tipoElemento): Actualiza un tipo de elemento por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un tipo de elemento por su ID.
 *
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
 *
 * @author Yariangel Aray
 */
public class TipoElementoDAO {

    /**
     * Obtiene todos los registros de tipos de elementos desde la base de datos.
     *
     * @return Lista de objetos TipoElemento con todos los tipos registrados.
     */
    public List<TipoElemento> getAll() {
        List<TipoElemento> tipos = new ArrayList<>(); // Lista para almacenar los resultados
        String SQL = "SELECT * FROM tipos_elementos ORDER BY id DESC"; // Consulta SQL

        // Bloque para ejecutar la consulta y recorrer los resultados
        try (
            Connection conexion = DBConnection.conectar(); // Establece conexión
            PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
            ResultSet rs = stmt.executeQuery() // Ejecuta la consulta y obtiene resultados
        ) {
            // Iteración sobre cada fila del resultado para mapearla a un objeto
            while (rs.next()) {
                TipoElemento tipo = mapearTipoElemento(rs); // Mapea la fila a un objeto TipoElemento
                tipos.add(tipo); // Agrega el objeto a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en consola
        }

        // Retorna la lista de tipos obtenidos
        return tipos;
    }

    /**
     * Busca un tipo de elemento por su ID.
     *
     * @param id El ID del tipo a buscar.
     * @return El objeto TipoElemento si se encuentra, o null si no existe.
     */
    public TipoElemento getById(int id) {
        TipoElemento tipo = null; // Inicializa el resultado
        String SQL = "SELECT * FROM tipos_elementos WHERE id = ?"; // Consulta con parámetro

        // Bloque para ejecutar la consulta con parámetro ID
        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el valor del parámetro
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Verifica si se obtuvo un resultado
            if (rs.next()) {
                tipo = mapearTipoElemento(rs); // Mapea el resultado
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retorna el tipo encontrado o null
        return tipo;
    }

    /**
     * Busca un tipo de elemento por su consecutivo (único).
     *
     * @param consecutivo El consecutivo único del tipo de elemento.
     * @return El objeto TipoElemento si se encuentra, o null si no existe.
     */
    public TipoElemento getByConsecutivo(int consecutivo) {
        TipoElemento tipo = null;
        String SQL = "SELECT * FROM tipos_elementos WHERE consecutivo = ?";

        // Bloque para ejecutar la consulta con parámetro consecutivo
        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setInt(1, consecutivo);
            ResultSet rs = stmt.executeQuery();

            // Verifica si se obtuvo un resultado
            if (rs.next()) {
                tipo = mapearTipoElemento(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retorna el tipo encontrado o null
        return tipo;
    }

    /**
     * Inserta un nuevo tipo de elemento en la base de datos.
     *
     * @param tipo TipoElemento con la información a registrar.
     * @return TipoElemento creado con el ID generado, o null si hubo error.
     */
    public TipoElemento create(TipoElemento tipo) {
        String SQL = "INSERT INTO tipos_elementos (nombre, consecutivo, descripcion, marca, modelo, atributos) VALUES (?, ?, ?, ?, ?, ?)";

        // Bloque para ejecutar la inserción y recuperar el ID generado
        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            // Asignación de parámetros al statement
            stmt.setString(1, tipo.getNombre());
            stmt.setInt(2, tipo.getConsecutivo());
            stmt.setString(3, tipo.getDescripcion());
            stmt.setString(4, tipo.getMarca());
            stmt.setString(5, tipo.getModelo());
            stmt.setString(6, tipo.getAtributos());

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la inserción

            // Verifica si se insertó correctamente
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys(); // Obtiene el ID generado
                if (generatedKeys.next()) {
                    tipo.setId(generatedKeys.getInt(1)); // Asigna el ID al objeto
                    return tipo; // Retorna el objeto creado
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retorna null si hubo error
        return null;
    }

    /**
     * Actualiza un tipo de elemento existente por su ID.
     *
     * @param id ID del tipo a actualizar.
     * @param tipo Objeto TipoElemento con los nuevos datos.
     * @return TipoElemento actualizado si fue exitoso, o null si falló.
     */
    public TipoElemento update(int id, TipoElemento tipo) {
        String SQL = "UPDATE tipos_elementos SET nombre = ?, consecutivo = ?, descripcion = ?, marca = ?, modelo = ?, atributos = ? WHERE id = ?";

        // Bloque para ejecutar la actualización
        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            // Asignación de nuevos valores
            stmt.setString(1, tipo.getNombre());
            stmt.setInt(2, tipo.getConsecutivo());
            stmt.setString(3, tipo.getDescripcion());
            stmt.setString(4, tipo.getMarca());
            stmt.setString(5, tipo.getModelo());
            stmt.setString(6, tipo.getAtributos());
            stmt.setInt(7, id);

            int filasAfectadas = stmt.executeUpdate(); // Ejecuta la actualización

            // Verifica si se actualizó correctamente
            if (filasAfectadas > 0) {
                tipo.setId(id); // Actualiza el ID en el objeto
                return tipo; // Retorna el objeto actualizado
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retorna null si hubo error
        return null;
    }

    /**
     * Elimina un tipo de elemento de la base de datos por su ID.
     *
     * @param id ID del tipo a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        String SQL = "DELETE FROM tipos_elementos WHERE id = ?";

        // Bloque para ejecutar la eliminación
        try (
            Connection conexion = DBConnection.conectar();
            PreparedStatement stmt = conexion.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id); // Asigna el ID al parámetro
            return stmt.executeUpdate() > 0; // Retorna true si se eliminó

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo error
        }
    }

    /**
     * Mapea un ResultSet a un objeto TipoElemento.
     *
     * @param rs ResultSet con los datos de una fila.
     * @return Objeto TipoElemento con los datos mapeados.
     * @throws SQLException Si ocurre un error al acceder a los datos.
     */
    private TipoElemento mapearTipoElemento(ResultSet rs) throws SQLException {
        // Bloque que convierte una fila del ResultSet en un objeto TipoElemento
        return new TipoElemento(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getInt("consecutivo"),
            rs.getString("descripcion"),
            rs.getString("marca"),
            rs.getString("modelo"),
            rs.getString("atributos")
        );
    }
}