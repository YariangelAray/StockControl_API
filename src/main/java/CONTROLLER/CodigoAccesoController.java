package controller;

import service.CodigoAccesoService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import providers.ResponseProvider;

/**
 * Controlador REST para gestionar códigos de acceso a inventarios.
 * 
 * Endpoints:
 * - POST /codigos-acceso/generar: Genera un código (admin)
 * - GET /codigos-acceso/validar/{codigo}: Valida un código (usuario)
 * 
 * @author Yariangel Aray
 */
@Path("/codigos-acceso")
public class CodigoAccesoController {
    
    CodigoAccesoService service; // Instancia del servicio que maneja la lógica de negocio

    public CodigoAccesoController() {
        // Instancia el servicio encargado de la lógica de negocio
        service = new CodigoAccesoService();
    }    

    @POST
    @Path("/generar/{inventarioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarCodigo(@PathParam("inventarioId") int inventarioId, HorasDTO horas) {
        try {
            return service.generarCodigo(inventarioId, horas.horas);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    @GET
    @Path("/validar/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarCodigo(@PathParam("codigo") String codigo) {
        try {
            return service.validarCodigo(codigo);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }
    
    @GET
    @Path("/inventario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCodigoActivo(@PathParam("id") int id) {
        try {
            return service.obtenerCodigoActivo(id);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno en el servidor", 500);
        }
    }

}

class HorasDTO {
    public int horas;
}
