package model.entity;

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
    private int reporteId;      // ID del reporte asociado

    public Foto() {}

    public Foto(int id, String url, int reporteId) {
        this.id = id;
        this.url = url;
        this.reporteId = reporteId;
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

    public int getReporteId() {
        return reporteId;
    }

    public void setReporteId(int reporteId) {
        this.reporteId = reporteId;
    }
}
