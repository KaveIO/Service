package nl.kpmg.af.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.connection.exception.MongoAuthenticationException;
import nl.kpmg.af.datamodel.dao.AbstractDao;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.exception.ServiceInitializationException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

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
    private static MongoDatabase securityDatabase;

    private static Map<String, MongoDatabase> applicationDatabases = new HashMap();

    /**
     * Private default constructor to make object instantiation impossible.
     */
    private MongoDBUtil() {
    }

    public static <T extends AbstractDao> T getDao(final String applicationId, final Class<T> clazz)
                                                                                                    throws ApplicationDatabaseConnectionException {
        MongoDatabase applicationDatabase = getApplicationDatabase(applicationId);
        try {
            Constructor<T> constructor = clazz.getConstructor(MongoDatabase.class);
            return constructor.newInstance(applicationDatabase);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new ApplicationDatabaseConnectionException("Couldn't build Dao for given applicationDatabase", ex);
        }
    }

    /**
     * @return The actual mongoDatabase objects which is being managed by this utility.
     */
    public static synchronized MongoDatabase getApplicationDatabase(final String applicationId)
                                                                                               throws ApplicationDatabaseConnectionException {
        if (!applicationDatabases.containsKey(applicationId)) {

            try {
                MongoDatabase connection = getSecurityDatabase();
                DB database = connection.getDatabase();
                DBCollection collection = database.getCollection("applications");
                DBObject application = collection.findOne(new BasicDBObject("id", applicationId));

                if (application == null) {
                    throw new ApplicationDatabaseConnectionException("No application record with id '" + applicationId
                                                                     + "' could be found in the security database");
                } else {
                    Map<String, Object> databaseParameters = (Map<String, Object>) application.get("database");
                    String host = (String) databaseParameters.get("host");
                    Integer port = (Integer) databaseParameters.get("port");
                    String username = (String) databaseParameters.get("username");
                    String password = (String) databaseParameters.get("password");
                    String databaseName = (String) databaseParameters.get("database");

                    applicationDatabases.put(applicationId,
                                             connectDatabase(host, port, username, password, databaseName));
                }
            } catch (UnknownHostException | MongoAuthenticationException ex) {
                throw new ApplicationDatabaseConnectionException(
                                                                 "Couldn't connect application database since no "
                                                                         + "connection to the security database could be established",
                                                                 ex);
            } catch (ClassCastException ex) {
                throw new ApplicationDatabaseConnectionException(
                                                                 "Couldn't connect application database since the "
                                                                         + "configuration in the security database couldn't be parsed.",
                                                                 ex);
            }
        }
        return applicationDatabases.get(applicationId);
    }

    /**
     * @return The actual mongoDatabase objects which is being managed by this utility.
     */
    private static synchronized MongoDatabase getSecurityDatabase() {
        if (securityDatabase == null) {
            String path = System.getProperty(CONFIG_DIR_PROPERTY) + File.separator + PROPERTIES_FILE_NAME;
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                properties.load(fileInputStream);

                String url = properties.getProperty("url");
                int port = Integer.parseInt(properties.getProperty("port"));
                String username = properties.getProperty("username");
                String password = properties.getProperty("password");
                String database = properties.getProperty("database");
                securityDatabase = connectDatabase(url, port, username, password, database);
            } catch (IOException ex) {
                throw new ServiceInitializationException(
                                                         "Can't load mongo.properties. Please make sure this is "
                                                                 + "available in your config dir. Redeployment of the Service is necessary for correct "
                                                                 + "operation.", ex);
            }
        }
        return securityDatabase;
    }

    private static MongoDatabase connectDatabase(final String url, final int port, final String username,
                                                 final String password, final String database) {
        return new MongoDatabase(url, port, username, password, database);
    }
}
