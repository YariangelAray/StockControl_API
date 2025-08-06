package controller.campos;

import model.Campo;
import java.util.ArrayList;
import java.util.List;

/**
 * Define los campos esperados y sus reglas de validación para la entidad "TipoElemento".
 * 
 * Esta clase proporciona una lista de objetos `Campo` que detallan los campos que se deben
 * validar en las peticiones relacionadas con tipos de elementos (crear, actualizar, etc.).
 * 
 * Se usa en conjunto con la clase `Validar` para procesar solicitudes JSON
 * y verificar que todos los campos cumplen con lo que se espera.
 * 
 * @author Yariangel Aray
 */
public class CamposTipoElemento {

    /**
     * Retorna la lista de campos a validar para un objeto de tipo TipoElemento.
     * 
     * @return Lista de campos con sus validaciones.
     */
    public static List<Campo> obtener() {
        List<Campo> campos = new ArrayList<>();

        // Campo obligatorio, nombre del tipo de elemento, entre 3 y 50 caracteres
        campos.add(new Campo("nombre", true, 3, 50, "string"));
        
        // Campo obligatorio, consecutivo del tipo de elemento, entre 1 y 50 caracteres
        campos.add(new Campo("consecutivo", true, 1, 10, "numero"));

        // Campo opcional, descripción del tipo, hasta 250 caracteres
        campos.add(new Campo("descripcion", false, 0, 250, "string"));

        // Campo opcional, marca del tipo, entre 2 y 50 caracteres
        campos.add(new Campo("marca", false, 0, 50, "string"));

        // Campo opcional, modelo del tipo, entre 2 y 50 caracteres
        campos.add(new Campo("modelo", false, 0, 50, "string"));

        // Campo obligatorio, atributos generales
        campos.add(new Campo("atributos", true, 3, 250, "string"));

        return campos;
    }
}
