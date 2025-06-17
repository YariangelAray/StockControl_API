package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para establecer la conexión con la base de datos MySQL.
 * 
 * Esta clase contiene la configuración de acceso a la base de datos 
 * y proporciona un método estático para obtener una conexión válida.
 * 
 * El método conectar() puede ser reutilizado en cualquier parte del proyecto.
 * 
 * Base de datos: stockcontrol_api
 * Driver: com.mysql.cj.jdbc.Driver (MySQL Connector/J)
 * 
 * 
 * @author Yariangel Aray
 */
public class DBConnection {

    // URL de conexión a la base de datos.
    private static final String URL = "jdbc:mysql://localhost:3306/stockcontrol_bd";

    // Credenciales del usuario
    private static final String USER = "Yari04";    
    private static final String PASSWORD = "0421";

    /**
     * Establece y retorna una conexión a la base de datos.
     *
     * @return Un objeto Connection si la conexión fue exitosa; null en caso contrario.
     */
    public static Connection conectar() {
        
        Connection connect = null;
        
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión usando DriverManager
            connect = DriverManager.getConnection(URL, USER, PASSWORD);

            // Mensaje de éxito en consola
            System.out.println("CONEXIÓN EXITOSA A LA BASE DE DATOS.");   
            
        } catch (SQLException e) {
            // Error al intentar conectarse (usuario, contraseña, URL mal, etc.)
            System.out.println("ERROR DE CONEXIÓN - " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            // Error si no se encuentra el driver JDBC
            System.out.println("NO SE ENCONTRÓ EL DRIVER - " + ex.getMessage());
        }

        // Retorna la conexión, o null si falló
        return connect;
    }
}