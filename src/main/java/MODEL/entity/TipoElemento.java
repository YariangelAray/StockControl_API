package model.entity;

/**
 * Clase que representa la entidad TipoElemento del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'tipos_elementos'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del TipoElementoDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * Autor: Yariangel Aray
 */
public class TipoElemento {
    private int id;
    private String nombre;
    private int consecutivo;
    private String descripcion;
    private String marca;
    private String modelo;
    private String atributos;
    private int cantidadElementos;

    public TipoElemento() {}    

    public TipoElemento(int id, String nombre, int consecutivo, String descripcion, String marca, String modelo, String atributos) {
        this.id = id;
        this.nombre = nombre;
        this.consecutivo = consecutivo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.atributos = atributos;
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

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }        

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }
    
    public int getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(int cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }
}
