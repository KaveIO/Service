/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.server;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fziliotto on 24-6-16.
 */
public class ServerGrizzly implements Server {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrizzly.class);
	ApplicationContext context;
	private HttpServer server;

	public ServerGrizzly() {
	}

	@Override
	public void start() {
		context = new ClassPathXmlApplicationContext(new String[] { "appConfig.xml" });
		AppConfig config = context.getBean(AppConfig.class);
		String baseUri = "http://" + config.getServerHost() + ":" + config.getServerPort();

		DataServiceApplication app = new DataServiceApplication(config.getServerName());

		server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), app);

	}

	@Override
	public void stop() {
		server.shutdown();
	}
}
