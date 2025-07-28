package model.entity;

/*
 * @author YariangelAray
 */
public class AccesoTemporal {
    private int id;
    private int usuario_id;
    private int inventario_id;    

    public AccesoTemporal() {}

    public AccesoTemporal(int id, int usuario_id, int inventario_id) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.inventario_id = inventario_id;        
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

    public int getInventario_id() { 
        return inventario_id; 
    }
    public void setInventario_id(int inventario_id) { 
        this.inventario_id = inventario_id; 
    }

}
