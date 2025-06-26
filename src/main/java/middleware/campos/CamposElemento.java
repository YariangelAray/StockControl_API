package middleware.campos;

import middleware.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "Elemento".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con elementos (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 *
 * @author Yariangel Aray
 */
public class CamposElemento {

    /**
     * Retorna la lista de campos a validar para un objeto de tipo Elemento.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        List<Campo> campos = new ArrayList<>(); // Crea la lista de campos

        // Campo placa: obligatorio, tipo string (ya que puede ser largo), entre 1 y 20 dígitos
        campos.add(new Campo("placa", true, 1, 50, "numero"));

        // Campo serial: opcional, tipo string, máximo 50 caracteres
        campos.add(new Campo("serial", false, 0, 50, "string"));

        // Campo tipoElementoId: obligatorio, tipo entero
        campos.add(new Campo("tipoElementoId", true, 1, 2, "numero"));

        // Campo fechaAdquisicion: obligatorio, tipo string (validación personalizada pendiente)
        campos.add(new Campo("fechaAdquisicion", true, 10, 10, "fecha")); // Espera formato "yyyy-MM-dd"

        // Campo valorMonetario: obligatorio, tipo double (como string por JSON)
        campos.add(new Campo("valorMonetario", true, 1, 20, "numero"));

        // Campo estadoId: obligatorio, tipo entero
        campos.add(new Campo("estadoId", true, 1, 2, "numero"));

        // Campo estadoActivo: obligatorio, tipo booleano
        campos.add(new Campo("estadoActivo", true, 0, 0, "booleano"));

        // Campo ambienteId: obligatorio, tipo entero
        campos.add(new Campo("ambienteId", true, 1, 2, "numero"));

        // Campo inventarioId: obligatorio, tipo entero
        campos.add(new Campo("inventarioId", true, 1, 2, "numero"));

        return campos; // Retorna la lista de campos
    }
}
