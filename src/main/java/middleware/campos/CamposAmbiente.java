package middleware.campos;

import middleware.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "Ambiente".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con ambientes (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 * 
 * Autor: Yariangel Aray
 */
public class CamposAmbiente {

    /**
     * Retorna la lista de campos a validar para un objeto de tipo Ambiente.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        // Crea una nueva lista de campos
        List<Campo> campos = new ArrayList<>();

        // Agrega el campo 'nombre' como obligatorio, tipo string, con mínimo 3 y máximo 50 caracteres
        campos.add(new Campo("nombre", true, 3, 50, "string"));

        // Agrega el campo 'centro_id' como obligatorio, sin longitud, tipo entero
        campos.add(new Campo("centro_id", true, 1, 2, "int"));

        // Retorna la lista completa de campos
        return campos;
    }
}
