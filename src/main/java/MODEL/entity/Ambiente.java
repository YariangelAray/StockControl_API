package model.entity;

/**
 * Clase que representa la entidad Ambiente del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'ambientes'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del AmbienteDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Ambiente {
    private int id;
    private String nombre;
    private int centro_id;

    public Ambiente() {}

    public Ambiente(int id, String nombre, int centro_id) {
        this.id = id;
        this.nombre = nombre;
        this.centro_id = centro_id;
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

    public int getCentro_id() {
        return centro_id;
    }

    public void setCentro_id(int centro_id) {
        this.centro_id = centro_id;
    }
}
