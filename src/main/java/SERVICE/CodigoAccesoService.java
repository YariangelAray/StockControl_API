package service;

import java.sql.Timestamp;
import model.dao.CodigoAccesoDAO;
import model.entity.CodigoAcceso;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.UUID;
import model.dao.InventarioDAO;
import model.entity.Inventario;

/**
 * Servicio que gestiona la lógica de negocio para los códigos de acceso a inventarios.
 * 
 * @author Yariangel Aray
 */
public class CodigoAccesoService {

    CodigoAccesoDAO dao;
    InventarioDAO daoInventario;

    public CodigoAccesoService() {
        dao = new CodigoAccesoDAO();
        daoInventario = new InventarioDAO();
    }

    /**
     * Genera un nuevo código de acceso para un inventario válido por cierto número de horas.
     */
    public Response generarCodigo(int inventarioId, int horasValidez) {
        // Validar si ya hay un código activo para este inventario
        CodigoAcceso codigoExistente = dao.getCodigoActivo(inventarioId);
        if (codigoExistente != null) {
            return ResponseProvider.error("Ya existe un código activo para este inventario. Espere a que expire antes de generar uno nuevo.", 409);
        }
        
        String codigoGenerado = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Código simple         
        Timestamp expiracion = Timestamp.from(Instant.now().plusSeconds(horasValidez * 3600));

        CodigoAcceso codigo = new CodigoAcceso(codigoGenerado, inventarioId, expiracion);                
        
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
            encontrado.setInventario_nombre(inventario.getNombre());
            return ResponseProvider.success(encontrado, "Código válido", 200);
        }              

        return ResponseProvider.error("Código inválido o expirado", 404);
    }
    
    public Response obtenerCodigoActivo(int inventarioId) {        
        CodigoAcceso codigo = dao.getCodigoActivo(inventarioId);
        if (codigo == null) 
            return ResponseProvider.error("No hay código activo", 404);        

        return ResponseProvider.success(codigo, "Código activo del inventario", 201);
    }

}
