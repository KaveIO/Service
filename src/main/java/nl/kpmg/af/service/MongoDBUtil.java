package nl.kpmg.af.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.service.exception.ServiceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBUtil {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);
    private static final String PROPERTIES_FILE_NAME = "mongo.properties";
    private static final String CONFIG_DIR_PROPERTY = "jboss.server.config.dir";
    private static MongoDatabase mongoDatabase;

    static {
        String path = System.getProperty(CONFIG_DIR_PROPERTY) + File.separator + PROPERTIES_FILE_NAME;
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            properties.load(fileInputStream);

            String url = properties.getProperty("url");
            int port = Integer.parseInt(properties.getProperty("port"));
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String database = properties.getProperty("database");
            mongoDatabase = new MongoDatabase(url, port, username, password, database);
        } catch (IOException ex) {
            throw new ServiceInitializationException("Can't load mongo.properties. Please make sure this is available "
                    + "in your config dir. Redeployment of the Service is necessary for correct operation.", ex);
        }
    }

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
