package nl.kpmg.af.service.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.dao.AbstractDao;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

import nl.kpmg.af.service.data.security.Application;
import nl.kpmg.af.service.data.security.repository.ApplicationRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility class for initializing the MongoDatabase object. This is not the prettiest solution imaginable. For now it
 * does suffice though. This setup keeps all mongo connections pooled and keeps all jboss knowledge in the Service
 * package.
 *
 * @author Hoekstra.Maarten
 */
public final class MongoDBUtil {

    /**
     * The logger for this class.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MongoDBUtil.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    private final Map<String, MongoDatabase> applicationDatabases = new HashMap();

    /**
     * Methods for Mongo database object creation and connection.
     *
     */
    public <T extends AbstractDao> T getDao(final String applicationId, final Class<T> clazz)
            throws ApplicationDatabaseConnectionException {
        LOGGER.debug("MongoDBUtil classs, inside  the getDao method.");
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
     * @return The actual mongoDatabase objects which is being managed by this utility.
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
            Application application = applicationRepository.findOneByName(applicationId);

            if (application == null) {
                throw new ApplicationDatabaseConnectionException("No application record with id '" + applicationId
                        + "' could be found in the security database");
            } else {
                Application.Database database = application.getDatabase();

                applicationDatabases.put(applicationId, connectDatabase(
                        database.getHost(),
                        database.getPort(),
                        database.getUsername(),
                        database.getPassword(),
                        database.getDatabase()));
            }
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
