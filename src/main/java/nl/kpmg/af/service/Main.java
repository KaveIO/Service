
package nl.kpmg.af.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kpmg.af.service.server.Server;
import nl.kpmg.af.service.server.ServerGrizzly;

/**
 * Created by fziliotto on 24-6-16.
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://localhost:9000";
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		final Server server = new ServerGrizzly();
		server.start();

		LOGGER.info("Server started at location: {}, press a button to stop it", BASE_URI);
		System.in.read();
		server.stop();
	}
}
