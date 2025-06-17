package config;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Filtro para permitir solicitudes CORS en la API.
 * 
 * Este filtro se ejecuta en cada respuesta HTTP del servidor
 * y añade los encabezados necesarios para permitir solicitudes 
 * desde otros dominios (cross-origin).
 *
 * Esto es especialmente útil cuando el frontend está alojado 
 * en un dominio diferente al backend.
 * 
 * Se habilitan los métodos GET, POST, PUT, DELETE, OPTIONS y HEAD.
 * También se permiten encabezados comunes y credenciales.
 * 
 * La anotación @Provider indica que esta clase debe ser descubierta 
 * y registrada automáticamente como filtro.
 * 
 * @author Yariangel Aray
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    /**
     * Método que se ejecuta automáticamente en cada respuesta enviada desde la API.
     * Agrega los encabezados necesarios para permitir solicitudes CORS.
     *
     * @param requestContext  Contexto de la solicitud HTTP.
     * @param responseContext Contexto de la respuesta HTTP.
     * @throws IOException Excepción que puede ser lanzada durante el filtrado.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        
        // Permitir solicitudes desde cualquier origen (*).
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        
        // Permitir estos encabezados personalizados en las solicitudes.
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        
        // Permitir el uso de cookies y autenticación en las solicitudes si es necesario.
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        
        // Métodos HTTP permitidos para llamadas CORS.
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
