package model.dto;

/**
 * DTO para representar un ambiente con la cantidad de elementos que contiene en un inventario específico.
 * 
 * @author Yariangel Aray
 */
public class AmbienteDTO {
    private int id;
    private String nombre;
    private String mapa;
    private int cantidad_elementos;

    public AmbienteDTO(int id, String nombre, String mapa, int cantidad_elementos) {
        this.id = id;
        this.nombre = nombre;
        this.mapa = mapa;
        this.cantidad_elementos = cantidad_elementos;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMapa() {
        return mapa;
    }

    public void setMapa(String mapa) {
        this.mapa = mapa;
    }

    public int getCantidad_elementos() {
        return cantidad_elementos;
    }
}
