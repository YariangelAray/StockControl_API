package model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private int tipo_elemento_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha_adquisicion;
    private double valor_monetario;
    private int estado_id;
    private String observaciones;    
    private boolean estado_activo;
    private int ambiente_id;
    private int inventario_id;

    public Elemento() {}

    public Elemento(int id, long placa, String serial, int tipo_elemento_id, Date fecha_adquisicion, double valor_monetario, int estado_id, String observaciones, boolean estado_activo, int ambiente_id, int inventario_id) {
        this.id = id;
        this.placa = placa;
        this.serial = serial;
        this.tipo_elemento_id = tipo_elemento_id;
        this.fecha_adquisicion = fecha_adquisicion;
        this.valor_monetario = valor_monetario;
        this.estado_id = estado_id;
        this.observaciones = observaciones;
        this.estado_activo = estado_activo;
        this.ambiente_id = ambiente_id;
        this.inventario_id = inventario_id;
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

    public int getTipo_elemento_id() {
        return tipo_elemento_id;
    }

    public void setTipo_elemento_id(int tipo_elemento_id) {
        this.tipo_elemento_id = tipo_elemento_id;
    }

    public Date getFecha_adquisicion() {
        return fecha_adquisicion;
    }

    public void setFecha_adquisicion(Date fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }

    public double getValor_monetario() {
        return valor_monetario;
    }

    public void setValor_monetario(double valor_monetario) {
        this.valor_monetario = valor_monetario;
    }

    public int getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(int estado_id) {
        this.estado_id = estado_id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isEstado_activo() {
        return estado_activo;
    }

    public void setEstado_activo(boolean estado_activo) {
        this.estado_activo = estado_activo;
    }
    
    public int getAmbiente_id() {
        return ambiente_id;
    }

    public void setAmbiente_id(int ambiente_id) {
        this.ambiente_id = ambiente_id;
    }

    public int getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(int inventario_id) {
        this.inventario_id = inventario_id;
    }
}
