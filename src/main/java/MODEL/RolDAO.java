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
 * sobre la tabla 'roles' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos incluidos:
 * - getAll()
 * - getById(int id)
 * - create(Rol rol)
 * - update(int id, Rol rol)
 * - delete(int id)
 * 
 * Nota: No se implementa lógica de negocio aquí, solo acceso a datos.
 * 
 * @author Yariangel Aray
 */
public class RolDAO {
    /**
    * Obtiene todos los registros de roles desde la base de datos.
    *
    * @return Lista de objetos Rol con todos los roles registrados.
    */
    public List<Rol> getAll() {
        List<Rol> roles = new ArrayList<>();
        String SQL = "SELECT * FROM roles";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Rol rol = new Rol(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
                roles.add(rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }
    
    /**
    * Busca un rol por su ID.
    *
    * @param id El ID del rol a buscar.
    * @return El objeto Rol si se encuentra, o null si no existe.
    */
    public Rol getById(int id) {
        Rol rol = null;
        String SQL = "SELECT * FROM roles WHERE id = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                rol = new Rol(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rol;
    }
    
    /**
    * Inserta un nuevo rol en la base de datos.
    *
    * @param rol Objeto Rol con la información a registrar.
    * @return true si la operación fue exitosa, false en caso contrario.
    */
    public boolean create(Rol rol) {
        String SQL = "INSERT INTO roles (nombre) VALUES (?)";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, rol.getNombre());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
    * Actualiza un rol existente por su ID.
    *
    * @param id ID del rol a actualizar.
    * @param rol Objeto Rol con los nuevos datos.
    * @return true si se actualizó correctamente, false en caso contrario.
    */
    public boolean update(int id, Rol rol) {
        String SQL = "UPDATE roles SET nombre = ? WHERE id = ?";

        try (Connection conexion = DBConnection.conectar();
             PreparedStatement stmt = conexion.prepareStatement(SQL)) {

            stmt.setString(1, rol.getNombre());
            stmt.setInt(2, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
    * Elimina un rol de la base de datos por su ID.
    *
    * @param id El ID del rol a eliminar.
    * @return true si la eliminación fue exitosa, false si falló.
    */
    public boolean delete(int id) {
        String SQL = "DELETE FROM roles WHERE id = ?";
        
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.getAllByIdRol(id);

        if (usuarios != null && !usuarios.isEmpty())            
            return false;               
        
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
