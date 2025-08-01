package model.dao;

import model.entity.Usuario;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase DAO (Data Access Object) para realizar operaciones CRUD 
 * sobre la tabla 'usuarios' en la base de datos.
 * 
 * Esta clase utiliza la clase utilitaria DBConnection para establecer conexión con la base de datos.
 * 
 * Métodos disponibles:
 * - getAll(): Obtiene todos los usuarios.
 * - getAllByIdRol(int idRol): Obtiene todos los usuarios que pertenecen a un rol específico.
 * - getAllByIdTipoDocumento(int idTipoDocumento): Obtiene todos los usuarios que tienen un tipo de documento específico.
 * - getAllByIdFicha(int idFicha): Obtiene todos los usuarios que pertenecen a un número de ficha especifico
 * - getById(int id): Busca un usuario por su ID.
 * - getByDocumento(String documento): Busca un usuario por su número de documento.
 * - getByCorreo(String correo): Busca un usuario por su correo electrónico.
 * - create(Usuario usuario): Crea un nuevo usuario y retorna el objeto creado.
 * - update(int id, Usuario usuario): Actualiza un usuario por su ID y retorna el objeto actualizado.
 * - delete(int id): Elimina un usuario por su ID.
 * 
 * Esta clase no contiene lógica de negocio, solo acceso a datos.
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
        // Inicializa una lista para almacenar los usuarios
        List<Usuario> usuarios = new ArrayList<>();
        // Consulta SQL para seleccionar todos los usuarios
        String SQL = "SELECT * FROM usuarios";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL); // Prepara la consulta
             ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

            // Itera sobre los resultados obtenidos
            while (rs.next()) {
                // Crea un nuevo objeto Usuario a partir de los datos de la fila actual
                Usuario usuario = new Usuario(
                    rs.getInt("id"), // Obtiene el ID del usuario
                    rs.getString("nombres"), // Obtiene el nombre del usuario
                    rs.getString("apellidos"), // Obtiene el apellido del usuario
                    rs.getInt("tipo_documento_id"), // Obtiene el tipo de documento
                    rs.getString("documento"), // Obtiene el número de documento
                    rs.getInt("genero_id"), // Obtiene el ID del género
                    rs.getString("telefono"), // Obtiene el número de teléfono
                    rs.getString("correo"), // Obtiene el correo electrónico
                    rs.getInt("ficha_id"), // Obtiene el ID de la ficha
                    rs.getString("contrasena"), // Obtiene la contraseña
                    rs.getBoolean("activo"), // Obtiene el estado
                    rs.getInt("rol_id") // Obtiene el ID del rol
                );
                // Agrega el usuario a la lista
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna la lista de usuarios
        return usuarios;
    }

    /**
     * Obtiene todos los usuarios que pertenecen a un rol específico.
     *
     * @param idRol ID del rol.
     * @return Lista de usuarios con ese rol o vacía si no hay coincidencias.
     */
    public List<Usuario> getAllByIdRol(int idRol) {
        return getAllByCampo("rol_id", idRol); // Consulta por rol
    }
    
    /**
     * Obtiene todos los usuarios que tienen un tipo de documento especifico.
     *
     * @param idTipoDocumento ID del tipo de documento.
     * @return Lista de usuarios o vacío si no hay coincidencias.
     */
    public List<Usuario> getAllByIdTipoDocumento(int idTipoDocumento) {
        return getAllByCampo("tipo_documento_id", idTipoDocumento); // Consulta por tipo de documento
    }
    
    /**
    * Obtiene una lista de usuarios que tienen asignada una ficha específica.
    *
    * @param idFicha ID de la ficha a buscar.
    * @return Lista de usuarios asociados a la ficha, o lista vacía si no hay ninguno.
    */
    public List<Usuario> getAllByIdFicha(int idFicha) {
        return getAllByCampo("ficha_id", idFicha); // Consulta por ficha
    }

    /**
    * Obtiene todos los usuarios que tienen asignado un género específico.
    *
    * @param idGenero ID del género a buscar.
    * @return Lista de usuarios que pertenecen a ese género.
    */
    public List<Usuario> getAllByIdGenero(int idGenero) {
        return getAllByCampo("genero_id", idGenero); // Consulta por genero
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return El objeto Usuario si se encuentra, o null si no existe.
     */
    public Usuario getById(int id) {
        // Inicializa el objeto Usuario como null
        Usuario usuario = null;
        // Consulta SQL para seleccionar un usuario por ID
        String SQL = "SELECT * FROM usuarios WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Usuario a partir de los datos de la fila actual
                usuario = new Usuario(
                    rs.getInt("id"), // Obtiene el ID del usuario
                    rs.getString("nombres"), // Obtiene el nombre del usuario
                    rs.getString("apellidos"), // Obtiene el apellido del usuario
                    rs.getInt("tipo_documento_id"), // Obtiene el tipo de documento
                    rs.getString("documento"), // Obtiene el número de documento
                    rs.getInt("genero_id"), // Obtiene el ID del género
                    rs.getString("telefono"), // Obtiene el número de teléfono
                    rs.getString("correo"), // Obtiene el correo electrónico
                    rs.getInt("ficha_id"), // Obtiene el ID de la ficha
                    rs.getString("contrasena"), // Obtiene la contraseña
                    rs.getBoolean("activo"), // Obtiene el estado
                    rs.getInt("rol_id") // Obtiene el ID del rol
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el usuario encontrado o null si no existe
        return usuario;
    }

    /**
     * Busca un usuario por su número de documento.
     *
     * @param documento Número de documento del usuario.
     * @return Usuario correspondiente o null si no se encuentra.
     */
    public Usuario getByDocumento(String documento) {
        // Inicializa el objeto Usuario como null
        Usuario usuario = null;
        // Consulta SQL para seleccionar un usuario por documento
        String SQL = "SELECT * FROM usuarios WHERE documento = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro documento en la consulta
            stmt.setString(1, documento);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Usuario a partir de los datos de la fila actual
                usuario = new Usuario(
                    rs.getInt("id"), // Obtiene el ID del usuario
                    rs.getString("nombres"), // Obtiene el nombre del usuario
                    rs.getString("apellidos"), // Obtiene el apellido del usuario
                    rs.getInt("tipo_documento_id"), // Obtiene el tipo de documento
                    rs.getString("documento"), // Obtiene el número de documento
                    rs.getInt("genero_id"), // Obtiene el ID del género
                    rs.getString("telefono"), // Obtiene el número de teléfono
                    rs.getString("correo"), // Obtiene el correo electrónico
                    rs.getInt("ficha_id"), // Obtiene el ID de la ficha
                    rs.getString("contrasena"), // Obtiene la contraseña
                    rs.getBoolean("activo"), // Obtiene el estado
                    rs.getInt("rol_id") // Obtiene el ID del rol
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el usuario encontrado o null si no existe
        return usuario;
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario a buscar.
     * @return Usuario correspondiente o null si no se encuentra.
     */
    public Usuario getByCorreo(String correo) {
        // Inicializa el objeto Usuario como null
        Usuario usuario = null;
        // Consulta SQL para seleccionar un usuario por correo
        String SQL = "SELECT * FROM usuarios WHERE correo = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro correo en la consulta
            stmt.setString(1, correo);
            // Ejecuta la consulta y obtiene los resultados
            ResultSet rs = stmt.executeQuery();

            // Verifica si hay resultados
            if (rs.next()) {
                // Crea un nuevo objeto Usuario a partir de los datos de la fila actual
                usuario = new Usuario(
                    rs.getInt("id"), // Obtiene el ID del usuario
                    rs.getString("nombres"), // Obtiene el nombre del usuario
                    rs.getString("apellidos"), // Obtiene el apellido del usuario
                    rs.getInt("tipo_documento_id"), // Obtiene el tipo de documento
                    rs.getString("documento"), // Obtiene el número de documento
                    rs.getInt("genero_id"), // Obtiene el ID del género
                    rs.getString("telefono"), // Obtiene el número de teléfono
                    rs.getString("correo"), // Obtiene el correo electrónico
                    rs.getInt("ficha_id"), // Obtiene el ID de la ficha
                    rs.getString("contrasena"), // Obtiene la contraseña
                    rs.getBoolean("activo"), // Obtiene el estado
                    rs.getInt("rol_id") // Obtiene el ID del rol
                );
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }

        // Retorna el usuario encontrado o null si no existe
        return usuario;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario Usuario con la información a registrar.
     * @return Usuario creado con el ID generado, o null si hubo error.
     */
    public Usuario create(Usuario usuario) {
        // Consulta SQL para insertar un nuevo usuario
        String SQL = "INSERT INTO usuarios (nombres, apellidos, tipo_documento_id, documento, genero_id, telefono, correo, ficha_id, contrasena, rol_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) { // Prepara la consulta y permite obtener el ID generado

            // Establece los valores de los parámetros en la consulta
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

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se insertó al menos un registro
            if (filasAfectadas > 0) {
                // Obtiene las claves generadas (ID del nuevo usuario)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Establece el ID en el objeto usuario y lo retorna
                    usuario.setId(generatedKeys.getInt(1));
                    return usuario;
                }
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();            
        }
        // Retorna null si hubo un error al crear el usuario
        return null;
    }

    /**
     * Actualiza un usuario existente por su ID.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Objeto Usuario con los nuevos datos.
     * @return Usuario actualizado si fue exitoso, o null si falló.
     */
    public Usuario update(int id, Usuario usuario) {
        // Consulta SQL para actualizar un usuario existente
        String SQL = "UPDATE usuarios SET nombres = ?, apellidos = ?, tipo_documento_id = ?, documento = ?, genero_id = ?, telefono = ?, correo = ?, ficha_id = ?, contrasena = ?, rol_id = ? WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece los valores de los parámetros en la consulta
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
            stmt.setInt(11, id); // Establece el ID del usuario a actualizar

            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Verifica si se actualizó al menos un registro
            if (filasAfectadas > 0) {
                // Establece el ID en el objeto usuario y lo retorna
                usuario.setId(id);
                return usuario;
            }

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
        }
        // Retorna null si hubo un error al actualizar el usuario
        return null;
    }

    /**
     * Elimina un usuario de la base de datos por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa, false si falló.
     */
    public boolean delete(int id) {
        // Consulta SQL para eliminar un usuario por ID
        String SQL = "DELETE FROM usuarios WHERE id = ?";

        // Intenta establecer una conexión y ejecutar la consulta
        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            // Establece el valor del parámetro id en la consulta
            stmt.setInt(1, id);
            // Ejecuta la consulta y obtiene el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate();
            // Retorna true si se eliminó al menos un registro
            return filasAfectadas > 0;

        } catch (SQLException e) {
            // Imprime el error en caso de que ocurra una excepción SQL
            e.printStackTrace();
            return false; // Retorna false si hubo un error
        }
    }
    
    /**
    * Realiza una eliminación lógica (soft delete) de un usuario.
    *
    * En lugar de borrar el registro de la base de datos, este método actualiza el campo `activo` a `false`,
    * indicando que el usuario ha sido desactivado. Esta técnica permite conservar la información
    * del usuario para auditoría o posibles restauraciones futuras.
    *
    * @param id ID del usuario a desactivar (eliminar lógicamente).
    * @return true si se actualizó correctamente, false si hubo un error.
    */
   public boolean softDelete(int id) {
       // Consulta SQL para desactivar el usuario en lugar de eliminarlo
       String SQL = "UPDATE usuarios SET activo = false WHERE id = ?";

       try (Connection conexion = DBConnection.conectar();              // Conexión a la base de datos
           PreparedStatement stmt = conexion.prepareStatement(SQL)) {  // Prepara la sentencia SQL
       
           stmt.setInt(1, id);                                         // Asigna el ID al parámetro
           int filasAfectadas = stmt.executeUpdate();                  // Ejecuta la actualización
           return filasAfectadas > 0;                                  // Devuelve true si se modificó al menos una fila
       } catch (SQLException e) {
           e.printStackTrace();                                        // Imprime el error si ocurre
           return false;                                               // Retorna false en caso de fallo
       }
   }

    
    /**
     * Método auxiliar que realiza una consulta genérica por campo y valor.
     *
     * @param campo Nombre del campo a consultar.
     * @param value Valor que debe tener el campo.
     * @return Lista de usuarios que cumplen la condición.
     */
    private List<Usuario> getAllByCampo(String campo, int value) {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para resultados
        String SQL = "SELECT * FROM elementos WHERE " + campo + " = ?"; // Consulta dinámica

        try (Connection conexion = DBConnection.conectar(); // Conexión a la base de datos
             PreparedStatement stmt = conexion.prepareStatement(SQL)) { // Prepara la consulta

            stmt.setInt(1, value); // Asigna el valor al campo
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            while (rs.next()) { // Itera sobre resultados
                // Crea un nuevo objeto Usuario a partir de los datos de la fila actual
                Usuario usuario = new Usuario(
                    rs.getInt("id"), // Obtiene el ID del usuario
                    rs.getString("nombres"), // Obtiene el nombre del usuario
                    rs.getString("apellidos"), // Obtiene el apellido del usuario
                    rs.getInt("tipo_documento_id"), // Obtiene el tipo de documento
                    rs.getString("documento"), // Obtiene el número de documento
                    rs.getInt("genero_id"), // Obtiene el ID del género
                    rs.getString("telefono"), // Obtiene el número de teléfono
                    rs.getString("correo"), // Obtiene el correo electrónico
                    rs.getInt("ficha_id"), // Obtiene el ID de la ficha
                    rs.getString("contrasena"), // Obtiene la contraseña
                    rs.getBoolean("activo"), // Obtiene el estado
                    rs.getInt("rol_id") // Obtiene el ID del rol
                );
                // Agrega el usuario a la lista
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime error
        }
        return usuarios; // Retorna la lista
    }
}
