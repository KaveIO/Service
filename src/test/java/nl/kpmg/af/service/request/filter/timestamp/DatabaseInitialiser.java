/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.kpmg.af.service.request.filter.timestamp;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import nl.kpmg.af.datamodel.connection.DatabaseManager;
import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.dao.EventDao;
import nl.kpmg.af.datamodel.struct.SerializablePair;

import static org.junit.Assert.fail;

/**
 *
 * @author anaskar
 */
public class DatabaseInitialiser {

    public static final int MONGO_PORT = 20021;
    public static final int MONGO_SECURITY_PORT = 20022;
    public static final String MONGO_HOST = "localhost";

    public static final String OVERWATCH_DBUSER = "dbuser";
    public static final String OVERWATCH_DBUSER_PASSWORD = "dna123";

    public static final String MANAGEMENT_DBUSER = "dbuser";
    public static final String MANAGEMENT_DBUSER_PASSWORD = "dna123";

    public static final String WIFI_MANAGEMENT_DB = "wifiManagement";
    public static final String KANTINE_OVERWATCH_DB = "KantineOverwatch";
    public static final String OASE_OVERWATCH_DB = "OaseOverwatch";

    public static final String COLLECTION = "visit";

    private static MongodExecutable securityMongodExecutable;
    private static MongodExecutable mongodExecutable;
    private static MongodProcess securityMongod;
    private static MongodProcess mongod;
    private static MongoClient securityMongoClient;

    private static MongoClient mongoClient;

    private DatabaseManager overwatchDatabaseManager;
    private MongoDatabase managementDatabase;
    private static EventDao eventDao;

//    @PostConstruct
//    public void load() {
//        System.out.println("*********** inside the load method of Databaseinitializer ****************");
//        // Initialise your database here: create schema, use DBUnit to load data, etc.
//        try {
//            //startMongo();
////               initDBs();
//        } catch (Exception ex) {
//            if (mongoClient != null) {
//                mongoClient.close();
//            }
//            if (mongod != null) {
//                mongod.stop();
//            }
//            if (mongodExecutable != null) {
//                mongodExecutable.stop();
//            }
//            Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public static void startMongoLayerService() throws Exception {
        // create the security database
        MongodStarter securitydbstarter = MongodStarter.getDefaultInstance();
        securityMongodExecutable = securitydbstarter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V2_4)
                .net(new Net(MONGO_SECURITY_PORT, Network.localhostIsIPv6()))
                .build()
        );
        securityMongod = securityMongodExecutable.start();
        securityMongoClient = new MongoClient(MONGO_HOST, MONGO_SECURITY_PORT);
        
        BufferedReader jsonSecurityReader = Files.newBufferedReader(Paths.get(".", "src", "test", "resources", "appDatabaseDetails.json"), StandardCharsets.UTF_8);
        StringBuilder jsonSecurity;
        for (jsonSecurity = new StringBuilder(); jsonSecurityReader.ready();
                jsonSecurity.append(jsonSecurityReader.readLine())) {
        }
        
        BasicDBObject testsecurityDBList = (BasicDBObject) JSON.parse(jsonSecurity.toString());

        DB securityDatabase = securityMongoClient.getDB("security");
        securityDatabase.addUser("security","bla123".toCharArray());
        securityDatabase.createCollection("applications", new BasicDBObject())
                .insert(testsecurityDBList, securityDatabase.getWriteConcern());

        // CREATION OF Application DATBASE NAMED TEST
        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V2_4)
                .net(new Net(MONGO_PORT, Network.localhostIsIPv6()))
                .build()
        );
        mongod = mongodExecutable.start();
        mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
        BufferedReader jsonReader = Files.newBufferedReader(Paths.get(".", "src", "test", "resources", "visitLayerTest.json"), StandardCharsets.UTF_8);
        StringBuilder jsonTest;
        for (jsonTest = new StringBuilder(); jsonReader.ready(); jsonTest.append(jsonReader.readLine())) {
        }
        BasicDBList testDBList = (BasicDBList) JSON.parse(jsonTest.toString());

        DB testDatabase = mongoClient.getDB("test");
        testDatabase.addUser("admin","admin".toCharArray());
        testDatabase.createCollection("visitLayer", new BasicDBObject())
                .insert(testDBList.toArray(new BasicDBObject[testDBList.size()]), testDatabase.getWriteConcern());
    }

    public void setUp() {
        Map<String, Object> overwatchConfig = new HashMap<>();
        overwatchConfig.put("username", OVERWATCH_DBUSER);
        overwatchConfig.put("password", OVERWATCH_DBUSER_PASSWORD);
        List<SerializablePair<String, Integer>> urls = new ArrayList<SerializablePair<String, Integer>>();
        urls.add(new SerializablePair<String, Integer>(MONGO_HOST, MONGO_PORT));
        overwatchConfig.put("urls", urls);

        overwatchDatabaseManager = new DatabaseManager(Collections.unmodifiableMap(overwatchConfig));
        managementDatabase = new MongoDatabase(
                MONGO_HOST,
                MONGO_PORT,
                OVERWATCH_DBUSER,
                OVERWATCH_DBUSER_PASSWORD.toCharArray(),
                WIFI_MANAGEMENT_DB
        );
    }

    public void startMongoTimestamptest() {
        setUp();
        try {
            startMongo();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
        }
        initDBs();
    }

    public void startMongo() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V2_4)
                .net(new Net(MONGO_PORT, Network.localhostIsIPv6()))
                .build()
        );
        mongod = mongodExecutable.start();
        mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
    }

    public void initDBs() {
        try {
            // create 2 overwatch databases
            DB kantineOverwatch = mongoClient.getDB(KANTINE_OVERWATCH_DB),
                    oaseOverwatch = mongoClient.getDB(OASE_OVERWATCH_DB);

            // parse list of drone's nodes(BasicDBList) from droneNodes.json file
            BufferedReader jsonReader = Files.newBufferedReader(Paths.get(".", "src", "test", "resources", "droneNodes.json"), StandardCharsets.UTF_8);
            StringBuilder json;
            for (json = new StringBuilder(); jsonReader.ready(); json.append(jsonReader.readLine())) {
            }
            BasicDBList dronesDBList = (BasicDBList) JSON.parse(json.toString());

            // Add user for overwatch databases and insert data into droneNodes collection.
            for (DB db : new DB[]{kantineOverwatch, oaseOverwatch}) {
                db.addUser(OVERWATCH_DBUSER, OVERWATCH_DBUSER_PASSWORD.toCharArray());
                db.createCollection("droneNodes", new BasicDBObject())
                        .insert(dronesDBList.toArray(new BasicDBObject[dronesDBList.size()]), db.getWriteConcern());
            }
            // create management database
            DB wifiManagement = mongoClient.getDB(WIFI_MANAGEMENT_DB);
            wifiManagement.addUser(MANAGEMENT_DBUSER, MANAGEMENT_DBUSER_PASSWORD.toCharArray());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
