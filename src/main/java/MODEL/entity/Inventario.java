package model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
import java.util.List;


/**
 * Clase que representa la entidad Inventario del sistema.
 * 
 * Este modelo mapea la tabla 'inventarios' en la base de datos y se utiliza 
 * para operaciones CRUD (crear, leer, actualizar, eliminar) en conjunto con el DAO.
 * 
 * También actúa como un POJO (Plain Old Java Object), con atributos, constructores, 
 * métodos getters y setters.
 * 
 * Las fechas están anotadas para que al serializar a JSON se devuelvan en formato legible.
 * 
 * @author Yariangel Aray
 */
public class Inventario {
    private int id;
    private String nombre;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")    
    private Date fecha_creacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ultima_actualizacion;
    
    private double valor_monetario;
    private int cantidad_elementos;  // Total de elementos asociados
    private int ambientes_cubiertos; // Total de ambientes cubiertos

    private int usuario_admin_id;
    
    // Lista de elementos asociadas al inventario
    private List<Elemento> elementos;

    public Inventario() {}

    public Inventario(int id, String nombre, Date fecha_creacion, Date ultima_actualizacion, int usuario_admin_id) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.usuario_admin_id = usuario_admin_id;
        this.ultima_actualizacion = ultima_actualizacion;
    }

    public int getCantidad_elementos() {
        return cantidad_elementos;
    }

    public void setCantidad_elementos(int cantidad_elementos) {
        this.cantidad_elementos = cantidad_elementos;
    }

    public int getAmbientes_cubiertos() {
        return ambientes_cubiertos;
    }

    public void setAmbientes_cubiertos(int ambientes_cubiertos) {
        this.ambientes_cubiertos = ambientes_cubiertos;
    }
    
    public double getValor_monetario() {
        return valor_monetario;
    }

    public void setValor_monetario(double valor_monetario) {
        this.valor_monetario = valor_monetario;
    }

    public Date getUltima_actualizacion() {
        return ultima_actualizacion;
    }

    public void setUltima_actualizacion(Date ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getUsuario_admin_id() {
        return usuario_admin_id;
    }

    public void setUsuario_admin_id(int usuario_admin_id) {
        this.usuario_admin_id = usuario_admin_id;
    }
    
    public List<Elemento> getElementos() {
        return elementos;
    }

    public void setElementos(List<Elemento> elementos) {
        this.elementos = elementos;
    }
}
