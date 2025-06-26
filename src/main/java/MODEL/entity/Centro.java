package model.entity;

/**
 * Clase que representa la entidad Centro del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'centros'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del CentroDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Centro {
    private int id;
    private String nombre;
    private String direccion;
    private int ciudad_id;

    public Centro() {}

    public Centro(int id, String nombre, String direccion, int ciudad_id) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad_id = ciudad_id;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCiudad_id() {
        return ciudad_id;
    }

    public void setCiudad_id(int ciudad_id) {
        this.ciudad_id = ciudad_id;
    }
}
