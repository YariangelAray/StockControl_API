package middleware;

/**
 * Representa un campo que debe validarse dentro de una entidad (como "Usuario").
 * Se utiliza para definir las reglas de validación, como si es requerido,
 * su tipo (string, número o booleano), y su longitud mínima y máxima en caso de texto.
 * 
 * Esta clase sirve como una estructura de datos para describir las propiedades de
 * los campos que se van a validar en una solicitud JSON.
 * 
 * @author YariangelAray
 */
public class Campo {
    private String nombre;
    private boolean requerido;
    private int minimo;
    private int maximo;
    private String tipo; // "string", "numero", "booleano"

    public Campo(String nombre, boolean requerido, int minimo, int maximo, String tipo) {
        this.nombre = nombre;
        this.requerido = requerido;
        this.minimo = minimo;
        this.maximo = maximo;
        this.tipo = tipo;
    }
    
    public String getNombre() { 
        return nombre; 
    }
    public boolean isRequerido() { 
        return requerido; 
    }
    public int getMinimo() { 
        return minimo; 
    }
    public int getMaximo() { 
        return maximo; 
    }
    public String getTipo() { 
        return tipo; 
    }
}

