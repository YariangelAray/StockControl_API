package controller;

import model.entity.AccesoTemporal;
import service.AccesoTemporalService;
import providers.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author YariangelAray
 */
@Path("/accesos-temporales")
public class AccesoTemporalController {
    
    AccesoTemporalService service;

    public AccesoTemporalController() {
        service = new AccesoTemporalService();
    }

    @GET
    @Path("/inventario/{inventarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuariosConAcceso(@PathParam("inventarioId") int inventarioId) {
        try {
            return service.obtenerUsuariosConAcceso(inventarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno al obtener accesos", 500);
        }
    }

    @POST
    @Path("/acceder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarAcceso(AccesoTemporal acceso) {
        try {
            return service.registrarAcceso(acceso);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno al registrar acceso", 500);
        }
    }

    @GET
    @Path("/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInventariosConAccesPorUsuario(@PathParam("usuarioId") int usuarioId) {
        try {
            return service.obtenerInventariosConAccesPorUsuario(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno al obtener inventarios", 500);
        }
    }

    @DELETE
    @Path("/inventario/{inventarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarAccesos(@PathParam("inventarioId") int inventarioId) {
        try {
            return service.eliminarAccesosPorInventario(inventarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseProvider.error("Error interno al eliminar accesos", 500);
        }
    }
}

