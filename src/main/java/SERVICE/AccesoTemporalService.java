package service;

import java.util.ArrayList;
import model.dao.AccesoTemporalDAO;
import model.entity.AccesoTemporal;
import model.entity.Elemento;
import model.entity.Inventario;
import providers.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;
import model.dao.ElementoDAO;
import model.dao.InventarioDAO;

/**
 *
 * @author YariangelAray
 */
public class AccesoTemporalService {

    AccesoTemporalDAO dao;
    InventarioDAO daoInventario;

    public AccesoTemporalService() {
        dao = new AccesoTemporalDAO();
        daoInventario = new InventarioDAO();
    }
    
    public Response registrarAcceso(AccesoTemporal acceso) {
        boolean creado = dao.createAcceso(acceso);
        if (creado) {
            return ResponseProvider.success(null, "Acceso éxitoso", 201);
        }
        return ResponseProvider.error("Error al acceder al inventario", 400);
    }

    public Response obtenerInventariosActivos(int usuarioId) {
        List<Integer> inventarios_id = dao.getInventariosAccesoUsuario(usuarioId);
        if (inventarios_id.isEmpty()) {
            return ResponseProvider.error("No hay inventarios activos para este usuario", 404);
        }
        
        List<Inventario> inventarios = new ArrayList<>();
        for (Integer integer : inventarios_id) {
            inventarios.add(daoInventario.getById(integer));
        }
        
        ElementoDAO elementoDAO = new ElementoDAO();
        for (Inventario inventario : inventarios) {
            // Obtiene los elementos relacionados al inventario actual (por su ID)
            List<Elemento> elementos = elementoDAO.getAllByIdInventario(inventario.getId());
                        
            // Calcular y asignar el valor monetario total del inventario
            double totalValor = 0;
            for (Elemento elemento : elementos) {
                totalValor += elemento.getValor_monetario();  // Usa tu getter del campo
            }
            inventario.setValor_monetario(totalValor);
            inventario.setCantidad_elementos(elementos.size());
            inventario.setAmbientes_cubiertos(daoInventario.getAllAmbientesByInventario(inventario.getId()).size());            
        }
        
        return ResponseProvider.success(inventarios, "Inventarios con acceso obtenidos correctamente", 200);
    }

    public Response eliminarAccesosPorInventario(int inventarioId) {
        boolean eliminado = dao.deleteAccesosInventario(inventarioId);
        if (eliminado) {
            return ResponseProvider.success(null, "Accesos del inventario eliminados correctamente", 200);
        }
        return ResponseProvider.error("Error al eliminar los accesos del inventario", 500);
    }
}
