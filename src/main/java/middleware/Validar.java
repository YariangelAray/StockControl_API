package middleware;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        // Inicializa una lista para almacenar los errores encontrados
        List<String> errores = new ArrayList<>();

        // Recorremos todos los campos esperados
        for (Campo campo : campos) {
            String nombre = campo.getNombre(); // Obtiene el nombre del campo a validar

            // Si el campo no existe en el JSON
            if (!json.has(nombre)) {
                // Si es obligatorio, agregamos un error a la lista
                if (campo.isRequerido()) {
                    errores.add("El campo '" + nombre + "' es obligatorio.");
                }
                continue; // Si no es obligatorio, se ignora y se pasa al siguiente campo
            }

            Object valor = json.get(nombre); // Obtiene el valor del campo en el JSON

            // Dependiendo del tipo, se hace una validación diferente
            switch (campo.getTipo()) {
                case "string":
                    // Verifica si el valor es una cadena
                    if (!(valor instanceof String)) {
                        errores.add("El campo '" + nombre + "' debe ser de tipo texto.");
                    } else {
                        // Verifica la longitud mínima y máxima
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
                    
                    // Verifica si el valor es un número
                    if (!(valor instanceof Number)) {                        
                        errores.add("El campo '" + nombre + "' debe ser de tipo numérico.");
                    }
                    break;

                case "booleano":
                    // Verifica si el valor es un booleano
                    if (!(valor instanceof Boolean)) {
                        errores.add("El campo '" + nombre + "' debe ser de tipo booleano.");
                    }
                    break;
                
                case "fecha":
                    // Verifica que el valor sea un string
                    if (!(valor instanceof String)) {
                        errores.add("El campo '" + nombre + "' debe ser una cadena en formato fecha.");
                    } else {
                        String fechaStr = (String) valor;
                        if (fechaStr.length() != campo.getMinimo()) {
                            errores.add("El campo '" + nombre + "' debe tener exactamente " + campo.getMinimo() + " caracteres (formato yyyy-MM-dd).");
                        } else {
                            try {
                                // Intenta convertir el string a una fecha
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                sdf.setLenient(false); // No permite fechas inválidas como 2024-02-30
                                Date fecha = sdf.parse(fechaStr); // Si lanza error, es porque no es una fecha válida
                            } catch (Exception e) {
                                errores.add("El campo '" + nombre + "' no tiene un formato de fecha válido (yyyy-MM-dd).");
                            }
                        }
                    }
                    break;

                default:
                    // Si el tipo no es reconocido, se agrega un error
                    errores.add("Tipo no reconocido para el campo '" + nombre + "'.");
            }
        }

        // Retorna la lista de errores encontrados
        return errores;
    }
}
