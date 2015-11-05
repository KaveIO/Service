package nl.kpmg.af.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.connection.exception.MongoAuthenticationException;
import nl.kpmg.af.datamodel.dao.AbstractDao;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.logging.Logger;
import nl.kpmg.af.service.service.LayerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility class for initializing the MongoDatabase object. This is not the
 * prettiest solution imaginable. For now it does suffice though. This setup
 * keeps all mongo connections pooled and keeps all jboss knowledge in the
 * Service package.
 *
 * @author Hoekstra.Maarten
 */
public final class MongoDBUtil {

    /**
     * The actual mongoDatabase objects which is being managed by this utility.
     */
    private MongoDatabase securityDatabase;
    private final Map<String, MongoDatabase> applicationDatabases = new HashMap();
    private String url;
    private int port;
    private String username;
    private String password;
    private String database;

    /**
     * The logger for this class.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MongoDBUtil.class);

    /*
     Getter setter methods
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Default constructor for object initialization via spring setter
     * injection.
     *
     */
    public MongoDBUtil() {
        LOGGER.info("MongoDBUtil.java Inside the MongoDBUtil constructor.Constructing "
                + "the MongoDbUtil object via Spring setter injection.");
        this.securityDatabase = null;
    }

    /**
     * Methods for Mongo database object creation and connection.
     *
     */
    public <T extends AbstractDao> T getDao(final String applicationId, final Class<T> clazz)
            throws ApplicationDatabaseConnectionException {
        LOGGER.info("MongoDBUtil classs, inside  the getDao method.");
        this.securityDatabase = new MongoDatabase(this.url, this.port, this.username, this.password, this.database);
        MongoDatabase applicationDatabase = getApplicationDatabase(applicationId);
        try {
            Constructor<T> constructor = clazz.getConstructor(MongoDatabase.class);
            return constructor.newInstance(applicationDatabase);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ex) {
            throw new ApplicationDatabaseConnectionException("Couldn't build Dao for given applicationDatabase", ex);
        }
    }

    /**
     * @return The actual mongoDatabase objects which is being managed by this
     * utility.
     */
    public synchronized MongoDatabase getApplicationDatabase(final String applicationId)
            throws ApplicationDatabaseConnectionException {
        LOGGER.info(" inside  the getApplicationDatabase method.");
        if (!applicationDatabases.containsKey(applicationId)) {
            connectApplicationDatabase(applicationId);

        }
        return applicationDatabases.get(applicationId);
    }

    private void connectApplicationDatabase(String applicationId) throws ApplicationDatabaseConnectionException {
        LOGGER.info(" inside  the connectApplicationDatabase method.");
        try {
            DB database = securityDatabase.getDatabase();
            DBCollection collection = database.getCollection("applications");
            DBObject application = collection.findOne(new BasicDBObject("id", applicationId));

            if (application == null) {
                throw new ApplicationDatabaseConnectionException("No application record with id '" + applicationId
                        + "' could be found in the security database");
            } else {
                Map<String, Object> databaseParameters = (Map<String, Object>) application.get("database");
                String host = (String) databaseParameters.get("host");
                Integer port;
                try {
                    port = (Integer) databaseParameters.get("port");
                } catch (ClassCastException ex) {
                    // Try parsing it as double... some (older) versions of Mongo return
                    // ints as doubles...
                    Double portDbl = (Double) databaseParameters.get("port");
                    port = portDbl.intValue();
                }
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

    private MongoDatabase connectDatabase(final String url, final int port, final String username,
            final String password, final String database) {
        return new MongoDatabase(url, port, username, password, database);
    }
}
