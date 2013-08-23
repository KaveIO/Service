/**
 * 
 */
package nl.kpmg.af.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

import org.jboss.logging.Logger;

/**
 * @author janos4276
 *
 */
public abstract class AbstractDao {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractDao.class);
	
	//private static final String CONNECTION_STRING = "mongodb://overwatch:bla123@localhost:27017";
	private static final String CONNECTION_STRING = "localhost:27017";
	private static final String DB_NAME = "overwatch";
	
	private String connectionString;
	private String dbName;
	private static DB mongoDB;
	
	/**
	 * AbstractDao Constructor in which we connect to the MongoDB using
	 * a MongoClient.
	 * @param connectionString The MongoDB connection string
	 * @param dbName           The name of the MongoDB   
	 */
	public AbstractDao(String connectionString, String dbName){
		if (mongoDB == null) {
			// TODO Should get the connection string, db name, user, and password from config.		
			this.connectionString = connectionString.isEmpty()?CONNECTION_STRING:connectionString;
			this.dbName = dbName.isEmpty()?DB_NAME:dbName;
			
			MongoClient mongoClient = null;
			
			try {
				LOGGER.info("Connecting to Mongo");
				mongoClient = new MongoClient(this.connectionString);
				LOGGER.info("Connected to Mongo");
				mongoDB = mongoClient.getDB(this.dbName);
				boolean auth = mongoDB.authenticate("overwatch","bla123".toCharArray());
				if (auth) {
					LOGGER.info("Yippee I'm in!!");
				} else {
					LOGGER.info("Get off my lawn, Sonny!!");
				}
			} catch (UnknownHostException ex) {
				LOGGER.fatal("Failed to connect to database", ex);
			} 
		/* Do not close
		finally {
			mongoClient.close();
		}
		*/
		}
	}	

	/**
	 * @return the mongoDB
	 */
	public DB getMongoDB() {
		return mongoDB;
	}
	
	
}
