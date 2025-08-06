package model;

/*
 * @author YariangelAray
 */
public class AccesoTemporal {
    private int id;
    private int usuario_id;
    private int codigo_acceso_id;    

    public AccesoTemporal() {}

    public AccesoTemporal(int id, int usuario_id, int codigo_acceso_id) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.codigo_acceso_id = codigo_acceso_id;        
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public int getUsuario_id() { 
        return usuario_id; 
    }
    public void setUsuario_id(int usuario_id) { 
        this.usuario_id = usuario_id; 
    }

    public int getCodigo_acceso_id() { 
        return codigo_acceso_id; 
    }
    public void setCodigo_acceso_id(int codigo_acceso_id) { 
        this.codigo_acceso_id = codigo_acceso_id; 
    }

}
