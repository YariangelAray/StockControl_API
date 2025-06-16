package CONFIG;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("CONTROLLER", "CONFIG");
    }
}