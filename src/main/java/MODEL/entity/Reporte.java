package model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * Clase que representa la entidad Reporte del sistema.
 *
 * Este modelo se usa para mapear los datos provenientes de la tabla 'reportes'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del ReporteDAO.
 *
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * 
 * @author Yariangel Aray
 */
public class Reporte {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fecha;
    private String asunto;
    private String mensaje;
    private int usuario_id;
    private int elemento_id;

    public Reporte() {}

    public Reporte(int id, Date fecha, String asunto, String mensaje, int usuario_id, int elemento_id) {
        this.id = id;
        this.fecha = fecha;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.usuario_id = usuario_id;
        this.elemento_id = elemento_id;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(int elemento_id) {
        this.elemento_id = elemento_id;
    }
}
