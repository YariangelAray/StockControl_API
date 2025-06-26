package model.entity;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Clase que representa la entidad Elemento del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'elementos'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del ElementoDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Elemento {

    private int id;
    private long placa;
    private String serial;
    private int tipoElementoId;
    private Date fechaAdquisicion;
    private double valorMonetario;
    private int estadoId;
    private boolean estadoActivo;
    private int ambienteId;
    private int inventarioId;

    public Elemento() {}

    public Elemento(int id, long placa, String serial, int tipoElementoId, Date fechaAdquisicion,
                    double valorMonetario, int estadoId, boolean estadoActivo, int ambienteId, int inventarioId) {
        this.id = id;
        this.placa = placa;
        this.serial = serial;
        this.tipoElementoId = tipoElementoId;
        this.fechaAdquisicion = fechaAdquisicion;
        this.valorMonetario = valorMonetario;
        this.estadoId = estadoId;
        this.estadoActivo = estadoActivo;
        this.ambienteId = ambienteId;
        this.inventarioId = inventarioId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPlaca() {
        return placa;
    }

    public void setPlaca(long placa) {
        this.placa = placa;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getTipoElementoId() {
        return tipoElementoId;
    }

    public void setTipoElementoId(int tipoElementoId) {
        this.tipoElementoId = tipoElementoId;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public double getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(double valorMonetario) {
        this.valorMonetario = valorMonetario;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public int getAmbienteId() {
        return ambienteId;
    }

    public void setAmbienteId(int ambienteId) {
        this.ambienteId = ambienteId;
    }

    public int getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(int inventarioId) {
        this.inventarioId = inventarioId;
    }
}
