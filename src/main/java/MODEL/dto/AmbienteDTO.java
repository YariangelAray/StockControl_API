package model.dto;

/**
 * DTO para representar un ambiente con la cantidad de elementos que contiene en un inventario específico.
 * 
 * @author Yariangel Aray
 */
public class AmbienteDTO {
    private int id;
    private String nombre;
    private int cantidad_elementos;

    public AmbienteDTO(int id, String nombre, int cantidad_elementos) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad_elementos = cantidad_elementos;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad_elementos() {
        return cantidad_elementos;
    }
}
