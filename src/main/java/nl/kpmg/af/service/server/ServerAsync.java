package nl.kpmg.af.service.server;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;

/**
 * Created by fziliotto on 6-7-16.
 */
public class ServerAsync implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrizzly.class);
    ApplicationContext context;
    private HttpServer server;

    public ServerAsync() {
    }

    @Override
    public void start() {
        context = new ClassPathXmlApplicationContext(new String[] { "appConfig.xml" });
        AppConfig config = context.getBean(AppConfig.class);


        HttpServer httpServer = new HttpServer();
        NetworkListener networkListener =
                new NetworkListener(config.getServerName(), config.getServerHost(), config.getServerPort());
        ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
                .setCorePoolSize(1).setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        networkListener.getTransport().setWorkerThreadPoolConfig(threadPoolConfig);
        httpServer.addListener(networkListener);


        DataServiceApplication app = new DataServiceApplication(config.getServerName());
        HttpHandler handler = ContainerFactory.createContainer(HttpHandler.class, app);
        httpServer.getServerConfiguration().addHttpHandler(handler, "/");

    /* Start the server threads*/
        try {
            httpServer.start();
            LOGGER.info("Grizzly server running.");
            Thread.currentThread().join();
        } catch (BindException be) {
            LOGGER.error("Cannot bind to port {}. Is it already in use?", config.getServerPort());
        } catch (IOException ioe) {
            LOGGER.error("IO exception while starting server.");
        } catch (InterruptedException ie) {
            LOGGER.info("Interrupted, shutting down.");
            httpServer.shutdown();
        }
    }

    @Override
    public void stop() {
        server.shutdown();
    }
}
