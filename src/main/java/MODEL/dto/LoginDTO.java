package model.dto;

/**
 * Esta clase representa un "DTO" (Data Transfer Objects) para el inicio de sesión.
 * 
 * Un DTO se usa para transportar datos entre el frontend
 * y el backend sin exponer directamente las entidades completas de la base de datos.
 * 
 * En este caso, `LoginDTO` se utiliza cuando un usuario intenta iniciar sesión. 
 * Contiene solo los datos necesarios para validar ese inicio de sesión:
 * - documento: su identificador único.
 * - contrasena: la clave que escribió.
 * - rol_id: el tipo de usuario que intenta ingresar (por ejemplo, administrador o instructor).
 * 
 * Esta clase es enviada desde el frontend al backend en formato JSON, y el backend usa estos datos
 * para verificar si el usuario existe, si su contraseña es correcta y si tiene permiso para entrar.
 * 
 * No contiene ninguna lógica ni conexión a base de datos.
 * 
 * @author Yariangel Aray
 */
public class LoginDTO {
    private int rol_id;
    private String documento;
    private String contrasena;

    public LoginDTO() { }

    // Constructor con parámetros para crear el objeto fácilmente
    public LoginDTO(int rol_id, String documento, String contrasena) {
        this.rol_id = rol_id;
        this.documento = documento;
        this.contrasena = contrasena;
    }        

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }        
}
