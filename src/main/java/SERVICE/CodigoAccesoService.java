package service;

import java.sql.Timestamp;
import model.dao.CodigoAccesoDAO;
import model.CodigoAcceso;
import utils.ResponseProvider;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import model.dao.AccesoTemporalDAO;
import model.dao.InventarioDAO;
import model.dto.HorasDTO;
import model.AccesoTemporal;
import model.Inventario;

/**
 * Servicio que gestiona la lógica de negocio para los códigos de acceso a inventarios.
 * 
 * @author Yariangel Aray
 */
public class CodigoAccesoService {

    CodigoAccesoDAO dao;
    InventarioDAO daoInventario;
    AccesoTemporalDAO daoAcceso;

    public CodigoAccesoService() {
        dao = new CodigoAccesoDAO();
        daoInventario = new InventarioDAO();
        daoAcceso = new AccesoTemporalDAO();
    }

    /**
     * Genera un nuevo código de acceso para un inventario válido por cierto número de horas.
     */
    public Response generarCodigo(int inventarioId, HorasDTO horasValidez) {
        // Validar si ya hay un código activo para este inventario
        CodigoAcceso codigoExistente = dao.getCodigoActivo(inventarioId);
        if (codigoExistente != null) {
            return ResponseProvider.error("Ya existe un código activo para este inventario. Espere a que expire antes de generar uno nuevo.", 409);
        }
        
        String codigoGenerado = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Código simple         
        Timestamp expiracion = Timestamp.from(Instant.now().plusSeconds(horasValidez.getHoras() * 3600 + horasValidez.getMinutos() * 60));

        CodigoAcceso codigo = new CodigoAcceso();                
        codigo.setCodigo(codigoGenerado);
        codigo.setInventario_id(inventarioId);
        codigo.setFecha_expiracion(expiracion);
        
        if (dao.create(codigo)) {
            return ResponseProvider.success(codigo, "Código generado exitosamente", 201);
        }

        return ResponseProvider.error("No se pudo generar el código", 500);
    }

    /**
     * Valida si un código existe y está activo.
     */
    public Response validarCodigo(String codigo) {
        CodigoAcceso encontrado = dao.searchValid(codigo);
        
        if (encontrado != null) {            
            Inventario inventario = daoInventario.getById(encontrado.getInventario_id());
            encontrado.setNombre_inventario(inventario.getNombre());
            return ResponseProvider.success(encontrado, "Código válido", 200);
        }              

        return ResponseProvider.error("Código inválido o expirado", 404);
    }
    
    public Response obtenerCodigoActivo(int inventarioId) {        
        CodigoAcceso codigo = dao.getCodigoActivo(inventarioId);
        if (codigo == null) 
            return ResponseProvider.error("No hay código activo", 204);        

        return ResponseProvider.success(codigo, "Código activo del inventario", 201);
    }

    public Response eliminarAccesosPorInventario(int inventarioId) {
        CodigoAcceso codigo = dao.getByIdInventario(inventarioId);
        if (codigo == null) {
            return ResponseProvider.success(null, "No hay código activo para este inventario", 204);
        }
        List<AccesoTemporal> accesos = daoAcceso.getAccesosPorCodigo(inventarioId);
        if (accesos.isEmpty()) {
            return ResponseProvider.success(null, "No hay usuarios con acceso para eliminar", 204);
        }
        
        boolean eliminadoAccesos = daoAcceso.deleteAccesosPorCodigo(codigo.getId());
        if (eliminadoAccesos) {
            boolean eliminado = dao.deleteCodigoPorInventario(inventarioId);
            if (eliminado) {
                return ResponseProvider.success(null, "Accesos del inventario eliminados correctamente", 200);                
            }
            return ResponseProvider.error("Error al eliminar el código de acceso", 500);                
        }
        return ResponseProvider.error("Error al eliminar los accesos del inventario", 500);
    }
}
