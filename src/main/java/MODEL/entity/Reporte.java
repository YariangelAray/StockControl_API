package model.entity;

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
    private Date fecha;
    private String asunto;
    private String mensaje;
    private int usuarioId;
    private int elementoId;

    public Reporte() {}

    public Reporte(int id, Date fecha, String asunto, String mensaje, int usuarioId, int elementoId) {
        this.id = id;
        this.fecha = fecha;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
        this.elementoId = elementoId;
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getElementoId() {
        return elementoId;
    }

    public void setElementoId(int elementoId) {
        this.elementoId = elementoId;
    }
}
