package controller;

import controller.ValidarCampos;
import model.Campo;                    
import java.io.IOException;           
import java.lang.reflect.Method;      
import java.util.List;               
import javax.ws.rs.container.*;      
import javax.ws.rs.core.Context;     
import javax.ws.rs.ext.Provider;     
import org.json.JSONObject;          

/**
 * Filtro que intercepta peticiones HTTP antes de que lleguen al método del controlador.
 * Se ejecuta automáticamente cuando un método tiene la anotación @ValidarCampos.
 * 
 * Proceso:
 * 1. Detecta si el método tiene @ValidarCampos
 * 2. Obtiene las reglas de validación para la entidad especificada
 * 3. Lee y parsea el JSON del cuerpo de la petición
 * 4. Valida los campos según las reglas
 * 5. Si hay errores, detiene la petición y devuelve error 400
 * 6. Si no hay errores, permite que continúe al método original
 * 
 * @author Yariangel Aray
 */

@Provider // JAX-RS registra esta clase como un filtro disponible
@ValidarCampos(entidad = "")
public class ValidarCamposFilter implements ContainerRequestFilter {
    // ContainerRequestFilter = interfaz que permite interceptar peticiones ANTES de llegar al método    
    
    @Context // Inyección automática de JAX-RS
    private ResourceInfo resourceInfo; // Contiene información sobre el método que está manejando la petición    
    
    /**
     * Este es el método que se ejecuta automaticamente antes de cada petición
     * que tenga un método marcado con @ValidarCampos.
     *      
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // requestContext = Contiene toda la información de la petición HTTP                       
        
        // Obtenemos el método que está manejando la solicitud actual
        Method metodo = resourceInfo.getResourceMethod();                
                
        
        // Obtenemos la anotación @ValidarCampos del método (si la tiene)
        ValidarCampos anotacion = metodo.getAnnotation(ValidarCampos.class);        
        
        if (anotacion == null) {
            return; // Si no hay anotación, no se realiza ninguna validación
                    // La petición continúa normalmente al método original                    
        }                
        
        // Obtenemos el nombre de la entidad a validar del parámetro de la anotación
        String entidad = anotacion.entidad(); // Ejemplo: "usuario", "producto", etc.        
        
        // Obtenemos la lista de campos y reglas definidos para esa entidad
        List<Campo> campos = RepositorioDeCampos.obtenerCampos(entidad);                
        
        if (campos == null) {
            // Si la entidad no está registrada en el repositorio, es un error de configuración
            requestContext.abortWith(ResponseProvider.error("Entidad no reconocida", 400));
            // Esto detiene la petición inmediatamente y devuelve error 400
            return; // No continúa con el procesamiento
        }
                
        
        // Leemos el cuerpo completo de la solicitud HTTP (el JSON que envió el cliente)
        String body = new String(requestContext.getEntityStream().readAllBytes());
        // Convierte el stream de bytes del cuerpo a String        
        
        JSONObject json; // Variable para almacenar el objeto JSON parseado                
        
        // Verificamos que el cuerpo sea un JSON válido
        try {
            json = new JSONObject(body); // Intenta crear un objeto JSON a partir del cuerpo
            // Si el JSON está mal formado, esto lanzará una excepción
        } catch (Exception e) {
            // Si hay un error al parsear el JSON (sintaxis incorrecta, etc.)
            requestContext.abortWith(ResponseProvider.error("JSON mal formado", 400));
            // Detiene la petición y informa que el JSON está mal formado
            return; // No continúa con la validación
        }
                
        
        // Validamos el contenido del JSON contra los campos y reglas esperados
        List<String> errores = Validar.validar(json, campos);
        // Esta es la validacion real que compara el JSON con las reglas        
                
        
        if (!errores.isEmpty()) {
            // Si encontramos errores de validación           
            // Detenemos la solicitud y devolvemos los errores al cliente
            requestContext.abortWith(ResponseProvider.error("Error de validación", 400, errores));
            // Esto hace que la petición se detenga aquí y muestre los errores que ocurrieron           
            return; // No se ejecuta más código
        }
                
        
        // Si llegamos aquí, significa que no hay errores de validación
        // Preparamos el cuerpo para que el método original pueda leerlo        
        // Volvemos a establecer el cuerpo en el contexto, ya que ya fue leído
        requestContext.setEntityStream(new java.io.ByteArrayInputStream(body.getBytes()));
        // Como ya leímos el stream para validar, lo "reconstruimos" para que el método original pueda leer el JSON cuando se ejecute
        // Es como "rebobinar" una cinta para que el siguiente la pueda usar
        
        // Al salir de este método sin llamar a abortWith(), la petición continúa
        // hacia el método original (crearUsuario, actualizarUsuario, etc.)        
    }
}