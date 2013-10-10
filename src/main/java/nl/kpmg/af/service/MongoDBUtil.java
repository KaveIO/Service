package nl.kpmg.af.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.service.exception.ServiceInitializationException;

/**
 * Utility class for initializing the MongoDatabase object.
 * This is not the prettiest solution imaginable. For now it does suffice though. This setup
 * keeps all mongo connections pooled and keeps all jboss knowledge in the Service package.
 * 
 * @author Hoekstra.Maarten
 */
public final class MongoDBUtil {
    /**
     * The name of the properties file.
     */
    private static final String PROPERTIES_FILE_NAME = "mongo.properties";
    /**
     * The configuration property name which refers to the configuration directory.
     */
    private static final String CONFIG_DIR_PROPERTY = "jboss.server.config.dir";
    /**
     * The actual mongoDatabase objects which is being managed by this utility.
     */
    private static MongoDatabase mongoDatabase;

    /**
     * Private default constructor to make object instantiation impossible.
     */
    private MongoDBUtil() {
    }

    /**
     * @return The actual mongoDatabase objects which is being managed by this utility.
     */
    public static synchronized MongoDatabase getMongoDatabase() {
        if (mongoDatabase == null) {
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

                // This will make sure all Layer, Node and Edge collections contain the correct Indexes.
                mongoDatabase.ensureIndexes();
            } catch (IOException ex) {
                throw new ServiceInitializationException("Can't load mongo.properties. Please make sure this is "
                        + "available in your config dir. Redeployment of the Service is necessary for correct "
                        + "operation.", ex);
            }
        }
        return mongoDatabase;
    }
}
