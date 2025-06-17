package middleware;

import providers.ResponseProvider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.json.JSONObject;

/**
 * Filtro que intercepta las peticiones HTTP y valida los campos del cuerpo JSON
 * si el método está anotado con @ValidarCampos.
 * 
 * Antes de que el método del recurso se ejecute, este filtro:
 * - Lee la anotación y obtiene la entidad.
 * - Consulta los campos que deben validarse para esa entidad.
 * - Lee el cuerpo (JSON) de la solicitud.
 * - Valida los campos según las reglas definidas.
 * - Si hay errores, detiene la petición y devuelve un error 400.
 * 
 * Este filtro se activa automáticamente si un método tiene la anotación @ValidarCampos.
 * 
 * @author YariangelAray
 */
@Provider
@ValidarCampos(entidad = "") // Requiere esta anotación para que se active
public class ValidarCamposFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo; // Permite acceder al método que maneja la petición

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Obtenemos el método que está manejando la solicitud actual
        Method metodo = resourceInfo.getResourceMethod();

        // Obtenemos la anotación del método (si no la tiene, no hacemos nada)
        ValidarCampos anotacion = metodo.getAnnotation(ValidarCampos.class);
        if (anotacion == null) {
            return;
        }

        // Obtenemos el nombre de la entidad a validar (ej. "usuario")
        String entidad = anotacion.entidad();

        // Obtenemos la lista de campos definidos para esa entidad
        List<Campo> campos = RepositorioDeCampos.obtenerCampos(entidad);
        if (campos == null) {
            // Si la entidad no está registrada, devolvemos un error
            requestContext.abortWith(ResponseProvider.error("Entidad no reconocida", 400));
            return;
        }

        // Leemos el cuerpo de la solicitud
        String body = new String(requestContext.getEntityStream().readAllBytes());
        JSONObject json;

        // Verificamos que el cuerpo sea un JSON válido
        try {
            json = new JSONObject(body);
        } catch (Exception e) {
            requestContext.abortWith(ResponseProvider.error("JSON mal formado", 400));
            return;
        }

        // Validamos el contenido del JSON contra los campos esperados
        List<String> errores = Validar.validar(json, campos);
        if (!errores.isEmpty()) {
            // Si hay errores de validación, detenemos la solicitud y los devolvemos
            requestContext.abortWith(ResponseProvider.error("Error de validación", 400, errores));
            return;
        }

        // Volvemos a establecer el cuerpo en el contexto, ya que ya fue leído
        requestContext.setEntityStream(new java.io.ByteArrayInputStream(body.getBytes()));
    }
}
