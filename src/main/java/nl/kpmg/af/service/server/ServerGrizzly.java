/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.server;

import java.io.IOException;

import nl.kpmg.af.service.server.HttpsServerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import nl.kpmg.af.service.server.HttpsServerProvider;
import nl.kpmg.af.service.server.ServerConfiguration;


/**
 * Created by sdowerah
 */
public class ServerGrizzly implements Server {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrizzly.class);
	ApplicationContext context;
	private final AppConfig configuration;
	private final BasicConfiguration config;
	private HttpsServerWrapper server;
	private final String baseUri;
	private final String baseFallbackUri;
	

	public ServerGrizzly() {
		context = new ClassPathXmlApplicationContext(new String[] { "appConfig.xml" });
		configuration = context.getBean(AppConfig.class);
		config = context.getBean(ServerConfiguration.class);
		
		baseUri = String.format("%s://%s:%d/", "https", config.getServiceHost(),
	    config.getSecureServicePort());
	    baseFallbackUri = String.format("%s://%s:%d/", "http", config.getServiceHost(),
	        config.getServicePort());
	    
	    
	}

	
	public HttpsServerWrapper startRestInterface() throws SslConfigurationException, IOException {
		DataServiceApplication app = new DataServiceApplication(configuration.getServerName());
		
		return HttpsServerProvider.createHttpsServer(config, baseUri, baseFallbackUri, app, false);
	}
	
	@Override
	public void start(){
	    try {
	      server = startRestInterface();
	    } catch (SslConfigurationException ex) {
	      LOGGER.error(
	          "Failed due to invalid SSL configuration", ex);
	    } catch (IOException ex) {
	      LOGGER.error(
	          "Failed starting due to the redirect server ", ex);
	      } 
	  };

	@Override
	public void stop() {
		server.stop();
	}
}