package controller.campos;

import model.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "TipoDocumento".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con tipos de documento (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 * 
 * @author Yariangel Aray
 */
public class CamposTipoDocumento {

    /**
     * Retorna la lista de campos a validar para un objeto de tipo TipoDocumento.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        // Crea una nueva lista de campos
        List<Campo> campos = new ArrayList<>();

        // Agrega el campo 'nombre' como obligatorio, tipo string, con mínimo 3 y máximo 50 caracteres
        campos.add(new Campo("nombre", true, 3, 50, "string"));

        // Retorna la lista completa de campos
        return campos;
    }
}
