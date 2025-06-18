package middleware.campos;

import middleware.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "Usuario".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con usuarios (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 * 
 * @author YariangelAray
 */
public class CamposUsuario {
    
    /**
     * Retorna la lista de campos a validar para un objeto de tipo Usuario.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        List<Campo> campos = new ArrayList<>();
        campos.add(new Campo("nombres", true, 3, 100, "string"));
        campos.add(new Campo("apellidos", true, 3, 100, "string"));
        campos.add(new Campo("tipo_documento_id", true, 1, 2, "numero"));
        campos.add(new Campo("documento", true, 10, 11, "string"));
        campos.add(new Campo("genero_id", true, 1, 2, "numero"));
        campos.add(new Campo("telefono", true, 8, 15, "string"));
        campos.add(new Campo("correo", true, 6, 100, "string"));
        campos.add(new Campo("ficha_id", false, 1, 2, "numero"));
        campos.add(new Campo("contrasena", true, 8, 50, "string"));
        campos.add(new Campo("rol_id", false, 1, 2, "numero"));        
        
        return campos;
    }
}
