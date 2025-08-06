package model;

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
    private int id;
    private String codigo;
    private String nombre_inventario;
    private int inventario_id;   
    private Timestamp fecha_expiracion;

    public CodigoAcceso() {}

    public CodigoAcceso(int id, String codigo, int inventario_id, Timestamp fecha_expiracion) {
        this.id = id;
        this.codigo = codigo;
        this.inventario_id = inventario_id;       
        this.fecha_expiracion = fecha_expiracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNombre_inventario() {
        return nombre_inventario;
    }

    public void setNombre_inventario(String nombre_inventario) {
        this.nombre_inventario = nombre_inventario;
    }

    public Timestamp getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(Timestamp fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }
}
