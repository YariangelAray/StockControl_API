package middleware.campos;

import middleware.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "Rol".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con roles (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 * 
 * @author Yariangel Aray
 */
public class CamposFicha {

    /**
     * Retorna la lista de campos a validar para un objeto de tipo Rol.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        // Crea una nueva lista de campos
        List<Campo> campos = new ArrayList<>();

        // Agrega el campo 'ficha' como obligatorio, tipo string, con mínimo 4 y máximo 10 caracteres
        campos.add(new Campo("ficha", true, 7, 20, "string"));

        // Retorna la lista completa de campos
        return campos;
    }
}
