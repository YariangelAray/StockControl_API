package model.dto;

/**
 * DTO que nos sirve para traer del frontend la cantidad de horas y minutos la cual estará disponible un inventario
 * cuando se le crea una llave de acceso
 * 
 * @author YariangelAray
 */
public class HorasDTO {
    private int horas;
    private int minutos;

    public HorasDTO() {}

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
}
