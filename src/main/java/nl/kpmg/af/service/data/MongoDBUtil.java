/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data;

import com.mongodb.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.dao.AbstractDao;
import nl.kpmg.af.service.data.core.repository.*;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

import nl.kpmg.af.service.data.security.Application;
import nl.kpmg.af.service.data.security.repository.ApplicationRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.TypeInformation;

import javax.validation.constraints.NotNull;

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

    private final Map<String, MeasurementRepository> measurementRepositoryCache = new HashMap();
    private final Map<String, ProxyRepository> proxyRepositoryCache = new HashMap();
    private final Map<String, RoleRepository> roleRepositoryCache = new HashMap();

    /**
     * Methods for Mongo database object creation and connection.
     *
     */
    @Deprecated
    public <T extends AbstractDao> T getDao(final String applicationId, final Class<T> clazz)
            throws ApplicationDatabaseConnectionException {
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
    @Deprecated
    public synchronized MongoDatabase getApplicationDatabase(final String applicationId)
            throws ApplicationDatabaseConnectionException {
        if (!applicationDatabases.containsKey(applicationId)) {
            connectApplicationDatabase(applicationId);

        }
        return applicationDatabases.get(applicationId);
    }

    @Deprecated
    private void connectApplicationDatabase(String applicationId) throws ApplicationDatabaseConnectionException {
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

    @Deprecated
    private MongoDatabase connectDatabase(final String url, final int port, final String username,
            final String password, final String database) {
        return new MongoDatabase(url, port, username, password, database);
    }

    public ProxyRepository getProxyRepository(String applicationId) throws ApplicationDatabaseConnectionException {
        ProxyRepository repository;

        if (proxyRepositoryCache.containsKey(applicationId)) {
            repository = proxyRepositoryCache.get(applicationId);
        } else {
            repository = getRepositoryForApplication(ProxyRepository.class, applicationId);
            proxyRepositoryCache.put(applicationId, repository);
        }

        return repository;
    }

    public RoleRepository getRoleRepository(String applicationId) throws ApplicationDatabaseConnectionException {
        RoleRepository repository;

        if (roleRepositoryCache.containsKey(applicationId)) {
            repository = roleRepositoryCache.get(applicationId);
        } else {
            repository = getRepositoryForApplication(RoleRepository.class, applicationId);
            roleRepositoryCache.put(applicationId, repository);
        }

        return repository;
    }

    private <T extends Repository> T getRepositoryForApplication(Class<T> repositoryInterface, String applicationId)
            throws ApplicationDatabaseConnectionException {

        Application application = applicationRepository.findOneByName(applicationId);
        if (application == null) {
            throw new ApplicationDatabaseConnectionException("No application record with id '" + applicationId
                    + "' could be found in the security database");
        } else {
            Application.Database database = application.getDatabase();

            try {
                MongoClientURI uri = geneateMongoUri(database.getUsername(),database.getPassword(),database.getHost(),database.getDatabase(),null);
                //this call sets also the authentication mechanism for mongo v3, which is SCRAM-SHA-1, while it v2 is MONGODB-CR
                //MongoClientURI uri = new MongoClientURI("mongodb://" + database.getUsername() + ":" + database.getPassword() + "@" + database.getHost() + ":" + database.getPort() + "/"+database.getDatabase()+"?authMechanism=SCRAM-SHA-1");

                SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(uri);

                // TODO To remove since it is never used
                //DbRefResolver dbRefResolver = new DefaultDbRefResolver(simpleMongoDbFactory);
                MongoTemplate mongoTemplate = new MongoTemplate(simpleMongoDbFactory);

                MongoRepositoryFactory repositoryFactory = new MongoRepositoryFactory(mongoTemplate);
                return repositoryFactory.getRepository(repositoryInterface);
            }  catch (UnknownHostException ex) {
                throw new ApplicationDatabaseConnectionException("Could not connect to application database", ex);
            }
        }
    }

    /**
     * Creates and returns a dynamic MeasurementRepository.
     *
     * In order to be a bit more flexible and fast in development we moved to spring-data for our DAO's. Our
     * multi-tenant and unified model setup however doesn't play to nice with this. This method is a factory for
     * building dynamic DAO's. In this way the Spring MongoTemple gets avoided (so we can switch db's) and the
     * MongoMappingContext gets overloaded a bit. Especially the last part makes the magic happen. The
     * MongoMappingContext is responsible for mapping object types to collections.
     *
     * @param applicationId of the application to create a DAO for
     * @param collection name fore which we create a DAO for
     * @return a dynamic DAO for given application and collection
     * @throws ApplicationDatabaseConnectionException
     */
    public MeasurementRepository getRepository(String applicationId, String collection) throws ApplicationDatabaseConnectionException {

        String repositoryIdentifier = applicationId + ":" + collection;
        MeasurementRepository repository;

        if (measurementRepositoryCache.containsKey(repositoryIdentifier)) {
            repository = measurementRepositoryCache.get(repositoryIdentifier);
        } else {
            Application application = applicationRepository.findOneByName(applicationId);
            if (application == null) {
                throw new ApplicationDatabaseConnectionException("No application record with id '" + applicationId
                        + "' could be found in the security database");
            } else {
                Application.Database database = application.getDatabase();

                try {
                    MongoClientURI uri = geneateMongoUri(database.getUsername(),database.getPassword(),database.getHost(),database.getDatabase(),null);

                    SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(uri);

                    MongoMappingContext mongoMappingContext = new StaticMongoMappingContext(collection);

                    DbRefResolver dbRefResolver = new DefaultDbRefResolver(simpleMongoDbFactory);
                    MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);

                    // In order to handle our flexible object model we have to add our own converters as well.
                    List coversions = new LinkedList();
                    coversions.add(new MeasurementReadConverter());
                    coversions.add(new MeasurementWriteConverter());
                    CustomConversions customConversions = new CustomConversions(coversions);
                    converter.setCustomConversions(customConversions);

                    converter.afterPropertiesSet();

                    MongoTemplate mongoTemplate = new MongoTemplate(simpleMongoDbFactory, converter);

                    MongoRepositoryFactory repositoryFactory = new MongoRepositoryFactory(mongoTemplate);
                    repository = repositoryFactory.getRepository(
                            MeasurementRepository.class,
                            new MeasurementRepositoryImpl(mongoTemplate, collection));

                    measurementRepositoryCache.put(repositoryIdentifier, repository);
                } catch (UnknownHostException ex) {
                    throw new ApplicationDatabaseConnectionException("Could not connect to application database", ex);
                }catch (Exception e){
                    throw new ApplicationDatabaseConnectionException("Could not connect to application database", e);

                }

            }
        }

        return repository;
    }

    private static class StaticMongoMappingContext extends MongoMappingContext {

        private final String collection;

        private StaticMongoMappingContext(String collection) {
            this.collection = collection;
        }

        @Override
        protected <T> BasicMongoPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
            BasicMongoPersistentEntity<T> entity = new StaticMongoPersistentEntity<T>(typeInformation, collection);

            return entity;
        }
    }

    private static class StaticMongoPersistentEntity<T> extends BasicMongoPersistentEntity {

        private final String collection;

        public StaticMongoPersistentEntity(TypeInformation typeInformation, String collection) {
            super(typeInformation);
            this.collection = collection;
        }

        @Override
        public String getCollection() {
            return collection;
        }

        /*
         In the future we want to be able to support queries on dynamic collection fields. For this to work we need
         correct MongoPersistentPropery objects. These objects are used to infer the datatype needed for the actual
         query.
         What we need to have here is a special mongo collection, a collection of collections so to say. This would
         contain the meta data describing the objects in each collection. This should be used to generate the correct
         persisten property objects.

         @Override
         public PersistentProperty getPersistentProperty(String name) {
         return new StaticMongoPersistentProperty(this, name);
         }
         */
    }

    /**
     * Generate a uri string to connect to a MongoDb instance in the format
     *    mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     *
     * @param username the user that access the database (if null the client will not try to connect to a database)
     * @param password tha password (only needed if username is specified, and optional for some authentication mechanism)
     * @param hosts the list of hosts (only host1 is mandatory), the hostname is in the form host[:port] to specify the port
     * @param database the name of the database
     * @return
     */
    public static MongoClientURI geneateMongoUri(@NotNull final String username, @NotNull final String password, @NotNull final List<String> hosts, final String database, final String options) throws UnknownHostException {
        if(hosts == null || hosts.size()==0 || hosts.get(0)==null || hosts.get(0).equals("")){
            throw new UnknownHostException("Unspecified host");
        }
        StringBuilder uriBuilder=new StringBuilder();
        //generate a mongo uri in the format
        uriBuilder.append("mongodb://");
        if(username != null && !username.equals("")){
            uriBuilder.append(username);
            if(password!=null){
                uriBuilder.append(":").append(password);
            }
            uriBuilder.append("@");
        }

        uriBuilder.append(hosts.get(0));

        for (int i = 1; i < hosts.size(); i++) {
            uriBuilder.append(",");
            uriBuilder.append(hosts.get(i));
        }
        if(database!=null){
            uriBuilder.append("/").append(database);
        }

        if(options!=null){
            if(database==null)
                uriBuilder.append("/");
            uriBuilder.append("?").append(options);
        }

        return new MongoClientURI(uriBuilder.toString());
    }

    /**
     * Generate a uri string to connect to a MongoDb instance in the format
     *    mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     *
     * This function takes a single string for the list of hosts.
     * @param username the user that access the database (if null the client will not try to connect to a database)
     * @param password tha password (only needed if username is specified, and optional for some authentication mechanism)
     * @param hosts the list of hosts (only host1 is mandatory), the hostname is in the form host[:port] to specify the port
     * @param database the name of the database
     * @return
     */
    public static MongoClientURI geneateMongoUri(@NotNull final String username, @NotNull final String password, @NotNull final String hosts, final String database, final String options) throws UnknownHostException {
        if(hosts == null || hosts.equals("")){
            throw new UnknownHostException("Unspecified host");
        }
        StringBuilder uriBuilder=new StringBuilder();
        //generate a mongo uri in the format
        uriBuilder.append("mongodb://");
        if(username != null && !username.equals("")){
            uriBuilder.append(username);
            if(password!=null){
                uriBuilder.append(":").append(password);
            }
            uriBuilder.append("@");
        }

        uriBuilder.append(hosts);

        if(database!=null){
            uriBuilder.append("/").append(database);
        }

        if(options!=null){
            if(database==null)
                uriBuilder.append("/");
            uriBuilder.append("?").append(options);
        }

        return new MongoClientURI(uriBuilder.toString());
    }
}
