package middleware;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de validar un objeto JSON contra una lista de campos esperados.
 * 
 * Compara el contenido del JSON recibido con las reglas definidas en cada objeto `Campo`.
 * Verifica si los campos existen, si son del tipo correcto, y si cumplen con la longitud esperada.
 * 
 * Devuelve una lista de errores encontrados (si los hay), o una lista vacía si todo está bien.
 * 
 * Se usa normalmente desde un filtro (`ContainerRequestFilter`) antes de procesar la solicitud.
 * 
 * @author Yariangel Aray
 */
public class Validar {

    /**
     * Valida un objeto JSON comparando sus campos con una lista de reglas definidas.
     * 
     * @param json El objeto JSON recibido en la solicitud.
     * @param campos Lista de campos que se deben validar.
     * @return Lista de errores encontrados (puede estar vacía si todo es válido).
     */
    public static List<String> validar(JSONObject json, List<Campo> campos) {
        List<String> errores = new ArrayList<>();

        // Recorremos todos los campos esperados
        for (Campo campo : campos) {
            String nombre = campo.getNombre();

            // Si el campo no existe en el JSON
            if (!json.has(nombre)) {
                // Si es obligatorio, agregamos un error
                if (campo.isRequerido()) {
                    errores.add("El campo '" + nombre + "' es obligatorio.");
                }
                continue; // Si no es obligatorio, se ignora
            }

            Object valor = json.get(nombre);

            // Dependiendo del tipo, se hace una validación diferente
            switch (campo.getTipo()) {
                case "string":
                    if (!(valor instanceof String)) {
                        errores.add("El campo '" + nombre + "' debe ser de tipo texto.");
                    } else {
                        int longitud = ((String) valor).length();
                        if (longitud < campo.getMinimo()) {
                            errores.add("El campo '" + nombre + "' debe tener al menos " + campo.getMinimo() + " caracteres.");
                        }
                        if (longitud > campo.getMaximo()) {
                            errores.add("El campo '" + nombre + "' debe tener como máximo " + campo.getMaximo() + " caracteres.");
                        }
                    }
                    break;

                case "numero":
                    if (!(valor instanceof Number)) {
                        errores.add("El campo '" + nombre + "' debe ser de tipo numérico.");
                    }
                    break;

                case "booleano":
                    if (!(valor instanceof Boolean)) {
                        errores.add("El campo '" + nombre + "' debe ser de tipo booleano.");
                    }
                    break;

                default:
                    errores.add("Tipo no reconocido para el campo '" + nombre + "'.");
            }
        }

        return errores;
    }
}
