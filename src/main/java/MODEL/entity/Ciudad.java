package model.entity;

/**
 * Clase que representa la entidad Ciudad del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'ciudades'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del CiudadDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Ciudad {
    private int id;
    private String nombre;

    public Ciudad() {}

    public Ciudad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}
