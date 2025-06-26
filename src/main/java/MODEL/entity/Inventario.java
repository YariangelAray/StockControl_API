package model.entity;

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
    private int usuarioAdminId;

    public Inventario() {}

    public Inventario(int id, String nombre, int usuarioAdminId) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioAdminId = usuarioAdminId;
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

    public int getUsuarioAdminId() {
        return usuarioAdminId;
    }

    public void setUsuarioAdminId(int usuarioAdminId) {
        this.usuarioAdminId = usuarioAdminId;
    }
}
