package service;

import java.util.ArrayList;
import model.dao.AccesoTemporalDAO;
import model.AccesoTemporal;
import model.Elemento;
import model.Inventario;
import utils.ResponseProvider;

import javax.ws.rs.core.Response;
import java.util.List;
import model.dao.CodigoAccesoDAO;
import model.dao.ElementoDAO;
import model.dao.InventarioDAO;
import model.CodigoAcceso;

/**
 *
 * @author YariangelAray
 */
public class AccesoTemporalService {

    AccesoTemporalDAO dao;
    InventarioDAO daoInventario;
    CodigoAccesoDAO daoCodigo;

    public AccesoTemporalService() {
        dao = new AccesoTemporalDAO();
        daoInventario = new InventarioDAO();
        daoCodigo = new CodigoAccesoDAO();
    }
    
    public Response obtenerUsuariosConAcceso(int inventarioId) {        
        CodigoAcceso codigo = daoCodigo.getCodigoActivo(inventarioId);  
        if (codigo == null) {
            return ResponseProvider.error("No hay código activo para este inventario", 404);
        }
        List<AccesoTemporal> accesos = dao.getAccesosPorCodigo(codigo.getId());
        if (accesos.isEmpty()) {
            return ResponseProvider.error("No hay usuarios con acceso temporal actualmente", 204);
        }
        return ResponseProvider.success(accesos, "Usuarios con acceso obtenidos correctamente", 200);
    }

    
    public Response registrarAcceso(String codigo, AccesoTemporal acceso) {
        // Paso 1: Buscar código válido
        CodigoAcceso encontrado = daoCodigo.searchValid(codigo);

        if (encontrado == null) {
            return ResponseProvider.error("Código inválido o expirado", 404);
        }

        // Paso 2: Validar que no exista ya el acceso
        List<Integer> accesos = dao.getInventariosAccesoUsuario(acceso.getUsuario_id());
        if (accesos.contains(encontrado.getId())) {
            return ResponseProvider.error("Este usuario ya cuenta con acceso al inventario", 409);
        }

        // Paso 3: Registrar acceso
        acceso.setCodigo_acceso_id(encontrado.getId());
        boolean creado = dao.createAcceso(acceso);

        if (creado) {
            // Adjuntar info extra del inventario (como lo hacía validarCodigo)
            Inventario inventario = daoInventario.getById(encontrado.getInventario_id());
            encontrado.setNombre_inventario(inventario.getNombre());
            return ResponseProvider.success(encontrado, "Acceso exitoso", 201);
        }

        return ResponseProvider.error("Error al registrar el acceso", 400);
    }


    public Response obtenerInventariosConAccesPorUsuario(int usuarioId) {
        List<Integer> inventarios_id = dao.getInventariosAccesoUsuario(usuarioId);
        if (inventarios_id.isEmpty()) {
            return ResponseProvider.success(null, "No hay inventarios activos para este usuario", 204);
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
}
