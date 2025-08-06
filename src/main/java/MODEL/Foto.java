package model;

/**
 * Clase que representa la entidad Foto del sistema.
 * Se relaciona con un reporte y almacena la URL donde se guarda la imagen.
 * 
 * Esta clase se usa para mapear los datos provenientes de la tabla 'fotos'
 * y facilita operaciones CRUD desde la capa DAO.
 * 
 * @author Yariangel Aray
 */
public class Foto {
    private int id;             // ID único de la foto
    private String url;         // Ruta o URL donde se almacena la imagen
    private int reporte_id;      // ID del reporte asociado

    public Foto() {}

    public Foto(int id, String url, int reporte_id) {
        this.id = id;
        this.url = url;
        this.reporte_id = reporte_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReporte_id() {
        return reporte_id;
    }

    public void setReporte_id(int reporte_id) {
        this.reporte_id = reporte_id;
    }
}
