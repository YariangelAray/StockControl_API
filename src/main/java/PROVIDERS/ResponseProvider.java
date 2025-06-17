package providers;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Clase utilitaria para construir respuestas HTTP uniformes para la API REST.
 * 
 * Esta clase proporciona métodos estáticos para generar respuestas de éxito o error, 
 * usando el objeto ResponseMessage como cuerpo de la respuesta.
 * 
 * Las respuestas generadas son de tipo JSON gracias al método entity(), y se construyen
 * completamente con build().
 * 
 * @author Yariangel Aray
 */
public class ResponseProvider {

    /**
     * Genera una respuesta exitosa con datos personalizados.
     * 
     * @param data    Objeto a enviar como contenido de la respuesta.
     * @param message Mensaje informativo para el cliente.
     * @param status  Código de estado HTTP (ej. 200, 201).
     * @return Objeto Response con estructura estandarizada.
     */
    public static Response success(Object data, String message, int status) {
        return Response.status(status)
                .entity(new ResponseMessage(true, status, message, data, null))
                .build();
    }

    /**
     * Genera una respuesta de error sin detalles adicionales.
     * 
     * @param message Mensaje explicando el error.
     * @param status  Código de estado HTTP (ej. 400, 404).
     * @return Objeto Response con estructura estandarizada de error.
     */
    public static Response error(String message, int status) {
        return Response.status(status)
                .entity(new ResponseMessage(false, status, message, null, null))
                .build();
    }

    /**
     * Genera una respuesta de error con una lista de errores detallados.
     * 
     * @param message Mensaje explicando el error general.
     * @param status  Código de estado HTTP.
     * @param errors  Lista de errores específicos o validaciones fallidas.
     * @return Objeto Response con estructura estandarizada de error.
     */
    public static Response error(String message, int status, List<String> errors) {
        return Response.status(status)
                .entity(new ResponseMessage(false, status, message, null, errors))
                .build();
    }
}

/**
 * Clase que representa la estructura del cuerpo de las respuestas HTTP.
 * 
 * Este objeto se convierte automáticamente a JSON y contiene los campos
 * comunes que pueden presentarse en una respuesta: éxito, código, mensaje,
 * datos, y errores.
 */
class ResponseMessage {
    private boolean success;
    private int code;
    private String message;
    private Object data;
    private List<String> errors;

    public ResponseMessage(boolean success, int code, String message, Object data, List<String> errors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
