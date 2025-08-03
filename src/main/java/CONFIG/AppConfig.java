package config;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.mindrot.jbcrypt.BCrypt;
import utils.DBConnection;

/**
 * Configuración principal de la API REST usando JAX-RS (Jersey).
 * 
 * Esta clase extiende ResourceConfig, lo que permite personalizar el comportamiento
 * de la aplicación REST, como registrar recursos, filtros y paquetes donde buscar
 * controladores o middleware.
 * 
 * La anotación @ApplicationPath define la ruta base de acceso a la API.
 * En este caso, todas las rutas estarán bajo el prefijo "/api".
 * 
 * Los paquetes registrados con .packages() son donde Jersey buscará automáticamente
 * clases con anotaciones como @Path, @Provider, etc.
 * 
 * @author Yariangel Aray
 */
@ApplicationPath("api")
public class AppConfig extends ResourceConfig {

    /**
     * Constructor que registra los paquetes donde se encuentran los recursos REST,
     * configuraciones y middleware de la aplicación.
     */
    public AppConfig() {                                  
        packages("controller", "config", "middleware");
        register(MultiPartFeature.class);
    }
}
