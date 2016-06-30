
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
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		final Server server = new ServerGrizzly();
		server.start();

		System.in.read();
		server.stop();
	}
}
