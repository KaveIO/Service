package nl.kpmg.af.service.server;

import static nl.kpmg.af.service.Main.BASE_URI;

import java.net.URI;

import nl.kpmg.af.service.security.CorsFilter;
import nl.kpmg.af.service.security.PermissionCheckerFilter2;
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

  public ServerGrizzly() {}


  @Override
  public void start() {
    context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});


    DataServiceApplication app = new DataServiceApplication();

    server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), app);

  }

  @Override
  public void stop() {
    server.shutdown();
  }
}


