package nl.kpmg.af.service.server;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import nl.kpmg.af.service.security.BasicAuthFilter;
import nl.kpmg.af.service.security.CorsFilter;
import nl.kpmg.af.service.security.PermissionCheckerFilter;

/**
 * Created by fziliotto on 29-6-16.
 */
public class DataServiceApplication extends ResourceConfig {

    public DataServiceApplication() {
        init();
    }

    public DataServiceApplication(String name) {
        this.setApplicationName(name);
        init();
    }

    private void init() {
        packages(true, "nl.kpmg.af.service.v0");
        packages(true, "nl.kpmg.af.service.v1");
        register(BasicAuthFilter.class);
        register(PermissionCheckerFilter.class);
        register(CorsFilter.class);
        registerClasses(JacksonFeature.class);
        registerClasses(JacksonJsonProvider.class);
    }
}
