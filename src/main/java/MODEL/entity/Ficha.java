package model.entity;

/**
 * Clase que representa la entidad Ficha del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'fichas'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del FichaDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Ficha {
    private int id;            // ID único de la ficha (clave primaria)
    private String ficha;         // Número de la ficha (código asignado)
    private int programa_id;    // ID del programa de formación asociado (clave foránea)

    public Ficha() {}
    
    public Ficha(int id, String ficha, int programa_id) {
        this.id = id;
        this.ficha = ficha;
        this.programa_id = programa_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public int getPrograma_id() {
        return programa_id;
    }

    public void setPrograma_id(int programa_id) {
        this.programa_id = programa_id;
    }
}