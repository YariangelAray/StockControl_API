package model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;

/**
 * Representa un código de acceso temporal a un inventario.
 * Este código es generado por un administrador y tiene un tiempo de expiración.
 * 
 * Está asociado a un inventario y se utiliza para permitir accesos temporales a usuarios normales.
 * 
 * @author Yariangel Aray
 */
public class CodigoAcceso {
    private String codigo;
    private int inventario_id;
    private String inventario_nombre;

    private Timestamp fecha_expiracion;

    public CodigoAcceso() {}

    public CodigoAcceso(String codigo, int inventario_id, Timestamp fecha_expiracion) {
        this.codigo = codigo;
        this.inventario_id = inventario_id;       
        this.fecha_expiracion = fecha_expiracion;
    }

    public String getInventario_nombre() {
        return inventario_nombre;
    }

    public void setInventario_nombre(String inventario_nombre) {
        this.inventario_nombre = inventario_nombre;
    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(int inventario_id) {
        this.inventario_id = inventario_id;
    }

    public Timestamp getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(Timestamp fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }
}
