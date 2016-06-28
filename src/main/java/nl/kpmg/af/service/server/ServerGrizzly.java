package nl.kpmg.af.service.server;

import static nl.kpmg.af.service.Main.BASE_URI;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import nl.kpmg.af.service.security.BasicAuthFilter;


/**
 * Created by fziliotto on 24-6-16.
 */
public class ServerGrizzly implements Server {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrizzly.class);
  ApplicationContext context;
  private HttpServer server;

  public ServerGrizzly() {
    context = new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});
  }


  @Override
  public void start() {

    // Adding the packages with the web resources
    ResourceConfig rc =
        new ResourceConfig().packages("nl.kpmg.af.service.v0", "nl.kpmg.af.service.v1")
            .property("ContextConfig", context).register(BasicAuthFilter.class);


    server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

  }

  @Override
  public void stop() {
    server.shutdown();
  }
}


