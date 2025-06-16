package MODEL;

import UTILS.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'usuarios' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos incluidos:
 * - getAll()
 * - getById(int id)
 * - getByDocumento(String documento)
 * - getByCorreo(String correo)
 * - create(Usuario usuario)
 * - update(int id, Usuario usuario)
 * - delete(int id)
 * 
 * Nota: No se implementa lógica de negocio aquí, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class UsuarioDAO {
    
    /**
    * Obtiene todos los registros de usuarios desde la base de datos.
    *
    * @return Lista de objetos Usuario con todos los usuarios registrados.
    */
    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String SQL = "SELECT * FROM usuarios";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getInt("tipo_documento_id"),
                    rs.getString("documento"),
                    rs.getInt("genero_id"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getObject("ficha_id") != null ? rs.getInt("ficha_id") : null,
                    rs.getString("contrasena"),
                    rs.getInt("rol_id")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    /**
    * Busca un usuario por su ID.
    *
    * @param id El ID del usuario a buscar.
    * @return El objeto Usuario si se encuentra, o null si no existe.
    */
    public Usuario getById(int id) {
        Usuario usuario = null;
        String SQL = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getInt("tipo_documento_id"),
                    rs.getString("documento"),
                    rs.getInt("genero_id"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getObject("ficha_id") != null ? rs.getInt("ficha_id") : null,
                    rs.getString("contrasena"),
                    rs.getInt("rol_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
    
    /**
    * Busca un usuario por su número de documento.
    *
    * @param documento El número de documento del usuario.
    * @return El objeto Usuario correspondiente, o null si no existe.
    */
    public Usuario getByDocumento(String documento) {
        Usuario usuario = null;
        String SQL = "SELECT * FROM usuarios WHERE documento = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getInt("tipo_documento_id"),
                    rs.getString("documento"),
                    rs.getInt("genero_id"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getObject("ficha_id") != null ? rs.getInt("ficha_id") : null,
                    rs.getString("contrasena"),
                    rs.getInt("rol_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
    
    /**
    * Busca un usuario por su correo electrónico.
    *
    * @param correo El correo del usuario a buscar.
    * @return El objeto Usuario correspondiente, o null si no existe.
    */
    public Usuario getByCorreo(String correo) {
        Usuario usuario = null;
        String SQL = "SELECT * FROM usuarios WHERE correo = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getInt("tipo_documento_id"),
                    rs.getString("documento"),
                    rs.getInt("genero_id"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getObject("ficha_id") != null ? rs.getInt("ficha_id") : null,
                    rs.getString("contrasena"),
                    rs.getInt("rol_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }        
    
    /**
    * Inserta un nuevo usuario en la base de datos.
    *
    * @param usuario Objeto Usuario con la información a registrar.
    * @return true si la operación fue exitosa, false en caso contrario.
    */
    public boolean create(Usuario usuario) {
        String SQL = "INSERT INTO usuarios (nombres, apellidos, tipo_documento_id, documento, genero_id, telefono, correo, ficha_id, contrasena, rol_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setInt(3, usuario.getTipo_documento_id());
            stmt.setString(4, usuario.getDocumento());
            stmt.setInt(5, usuario.getGenero_id());
            stmt.setString(6, usuario.getTelefono());
            stmt.setString(7, usuario.getCorreo());
            stmt.setInt(8, usuario.getFicha_id());
            stmt.setString(9, usuario.getContrasena());
            stmt.setInt(10, usuario.getRol_id());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
    * Actualiza un usuario existente por su ID.
    *
    * @param id ID del usuario a actualizar.
    * @param usuario Objeto Usuario con los nuevos datos.
    * @return true si se actualizó correctamente, false en caso contrario.
    */
    public boolean update(int id, Usuario usuario) {
        String SQL = "UPDATE usuarios SET nombres = ?, apellidos = ?, tipo_documento_id = ?, documento = ?, genero_id = ?, telefono = ?, correo = ?, ficha_id = ?, contrasena = ?, rol_id = ? WHERE id = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setInt(3, usuario.getTipo_documento_id());
            stmt.setString(4, usuario.getDocumento());
            stmt.setInt(5, usuario.getGenero_id());
            stmt.setString(6, usuario.getTelefono());
            stmt.setString(7, usuario.getCorreo());
            stmt.setInt(8, usuario.getFicha_id());
            stmt.setString(9, usuario.getContrasena());
            stmt.setInt(10, usuario.getRol_id());
            stmt.setInt(11, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
    * Elimina un usuario de la base de datos por su ID.
    *
    * @param id El ID del usuario a eliminar.
    * @return true si la eliminación fue exitosa, false si falló.
    */
    public boolean delete(int id) {
        String SQL = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}