package middleware.campos; 

import middleware.Campo; 
import java.util.ArrayList;
import java.util.List; 

/**
 * Define los campos esperados y sus reglas de validación para la entidad "Reporte".
 *
 * Esta clase proporciona una lista de objetos `Campo` que especifican los datos esperados
 * al crear o actualizar un reporte desde el cliente (frontend).
 *
 * No incluye la fecha porque esta se asigna automáticamente por la base de datos.
 * 
 * @author Yariangel
 */
public class CamposReporte {

    /**
     * Retorna la lista de campos que deben validarse para la entidad Reporte.
     * 
     * @return Lista de campos y sus reglas de validación.
     */
    public static List<Campo> obtener() {
        // Crea una nueva lista para almacenar los campos
        List<Campo> campos = new ArrayList<>();

        // Campo asunto: obligatorio, texto, entre 5 y 100 caracteres
        campos.add(new Campo("asunto", true, 5, 100, "string"));

        // Campo mensaje: obligatorio, tipo texto largo, mínimo 5 caracteres, sin máximo definido
        campos.add(new Campo("mensaje", true, 5, 250, "string"));

        // Campo usuario_id: obligatorio, numérico (representa al usuario que hace el reporte)
        campos.add(new Campo("usuario_id", true, 1, 2, "numero"));

        // Campo elemento_id: obligatorio, numérico (representa al elemento al que se le hace el reporte)
        campos.add(new Campo("elemento_id", true, 1, 2, "numero"));

        // Retorna la lista de campos configurados para validación
        return campos;
    }
}
