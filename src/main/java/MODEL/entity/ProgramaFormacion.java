package model.entity;

import java.util.List;

/**
 * Clase que representa la entidad ProgramaFormacion del sistema.
 *
 * Este modelo se usa para mapear los datos provenientes de la tabla 'programas_formacion'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del ProgramaFormacionDAO.
 *
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 *
 * Además, contiene una lista de fichas asociadas al programa para su consulta conjunta.
 *
 * @author Yariangel Aray
 */
public class ProgramaFormacion {
    
    private int id;
    private String nombre;
    
    // Lista de fichas asociadas a este programa de formación
    private List<Ficha> fichas;

    /**
     * Constructor vacío requerido para frameworks y serialización
     */
    public ProgramaFormacion() {}

    /**
     * Constructor con parámetros para facilitar la creación de objetos
     * @param id ID del programa
     * @param nombre Nombre del programa
     */
    public ProgramaFormacion(int id, String nombre) {
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

    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }
} 
