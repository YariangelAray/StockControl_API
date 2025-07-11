package model.entity;

import java.sql.Date;
import java.util.List;

/**
 * Clase que representa la entidad Inventario del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'inventarios'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del InventarioDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Inventario {
    private int id;
    private String nombre;
    private Date fecha_creacion;
    private int usuario_admin_id;
    
    // Lista de elementos asociadas al inventario
    private List<Elemento> elementos;

    public Inventario() {}

    public Inventario(int id, String nombre, Date fecha_creacion, int usuario_admin_id) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.usuario_admin_id = usuario_admin_id;
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
