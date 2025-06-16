package PROVIDERS;

import javax.ws.rs.core.*;

/**
 *
 * @author Yariangel Aray
 */

public class ResponseProvider {
    
    public static Response success(Object data, String message, int status) {
        return Response.status(status)
                .entity(new ResponseMessage(true, status, message, data))
                .build();
    }

    public static Response error(String message, int status) {
        return Response.status(status)
                .entity(new ResponseMessage(false, status, message, null))
                .build();
    }
}

class ResponseMessage {
    private boolean success;
    private int code;
    private String message;
    private Object data;
    
    public ResponseMessage(boolean success, int code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

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
}