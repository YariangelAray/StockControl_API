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

        // Campo placa: obligatorio, tipo string (ya que puede ser largo), entre 1 y 50 dígitos
        campos.add(new Campo("placa", true, 1, 50, "numero"));

        // Campo serial: opcional, tipo string, máximo 50 caracteres
        campos.add(new Campo("serial", false, 0, 50, "string"));

        // Campo tipoElementoId: obligatorio, tipo entero
        campos.add(new Campo("tipo_elemento_id", true, 1, 2, "numero"));

        // Campo fechaAdquisicion: obligatorio, tipo string
        campos.add(new Campo("fecha_adquisicion", true, 10, 10, "fecha"));

        // Campo valorMonetario: obligatorio, tipo double (como string por JSON)
        campos.add(new Campo("valor_monetario", true, 1, 20, "numero"));

        // Campo estadoId: opcional, tipo entero
        campos.add(new Campo("estado_id", false, 1, 2, "numero"));
        
        // Campo observaciones: opcional, tipo string
        campos.add(new Campo("observaciones", false, 0, 250, "string"));

        // Campo ambienteId: opcional, tipo entero
        campos.add(new Campo("ambiente_id", false, 1, 2, "numero"));

        // Campo inventarioId: obligatorio, tipo entero
        campos.add(new Campo("inventario_id", true, 1, 2, "numero"));

        return campos; // Retorna la lista de campos
    }
}
