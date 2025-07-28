package model.dto;
/**
 * DTO para operaciones relacionadas con contraseñas de usuarios.
 * 
 * Este objeto se utiliza tanto para cambiar la contraseña de un usuario,
 * como para verificar la contraseña actual al momento de desactivar su cuenta.
 * 
 * Si se desea cambiar la contraseña, ambos campos deben ser enviados.
 * Si se desea desactivar la cuenta, solo se debe enviar la contraseña actual.
 * 
 * Campo obligatorio: contrasena_actual.
 * Campo opcional (solo requerido al cambiarla): contrasena_nueva.
 * 
 * @author Yariangel Aray
 */

public class ContrasenaDTO {
    private String contrasena_actual;
    private String contrasena_nueva;

    public String getContrasena_actual() {
        return contrasena_actual;
    }

    public void setContrasena_actual(String contrasena_actual) {
        this.contrasena_actual = contrasena_actual;
    }

    public String getContrasena_nueva() {
        return contrasena_nueva;
    }

    public void setContrasena_nueva(String contrasena_nueva) {
        this.contrasena_nueva = contrasena_nueva;
    }
}
