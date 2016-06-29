package nl.kpmg.af.service.server;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import nl.kpmg.af.service.security.BasicAuthFilter;
import nl.kpmg.af.service.security.CorsFilter;
import nl.kpmg.af.service.security.PermissionCheckerFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Created by fziliotto on 29-6-16.
 */
public class DataServiceApplication  extends ResourceConfig {
    public DataServiceApplication(){
        packages(true,"nl.kpmg.af.service.v0");
        packages(true,"nl.kpmg.af.service.v1");
        register(BasicAuthFilter.class);
        register(PermissionCheckerFilter.class);
        register(CorsFilter.class);
        registerClasses(JacksonFeature.class);
        registerClasses(JacksonJsonProvider.class);
    }
}
