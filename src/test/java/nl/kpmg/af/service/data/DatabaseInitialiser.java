/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.mongodb.*;
import com.mongodb.util.JSON;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * @author anaskar
 */
public class DatabaseInitialiser {

    public static final String MONGO_HOST = "localhost";
    public static final int MONGO_PORT = 20021;

    public static final String MONGO_SECURITY_DATABASE = "security";
    public static final String MONGO_SECURITY_USER = "security";
    public static final String MONGO_SECURITY_PASSWORD = "bla123";

    public static final String MONGO_APPLICATION_DATABASE = "test";
    public static final String MONGO_APPLICATION_USER = "admin";
    public static final String MONGO_APPLICATION_PASSWORD = "admin";

    private MongoMockDatabase mockDatabase;

    public void start() throws Exception {
        try {
            mockDatabase = createMockDatabase(MONGO_HOST, MONGO_PORT);

            createMockUser(MONGO_SECURITY_DATABASE, MONGO_SECURITY_USER, MONGO_SECURITY_PASSWORD);
            createMockUser(MONGO_APPLICATION_DATABASE, MONGO_APPLICATION_USER, MONGO_APPLICATION_PASSWORD);
            loadMockData(Arrays.asList(
                    "security.applications.json",
                    "test.visitLayer.json",
                    "test.testA.json",
                    "test.testB.json",
                    "test.roles.json"));
        } catch (Exception ex) {
            stop();
            throw ex;
        }
    }

    public void stop() {
        if (mockDatabase != null) {
            if (mockDatabase.mongoClient != null) {
                mockDatabase.mongoClient.close();
            }
            if (mockDatabase.mongodProcess != null) {
                mockDatabase.mongodProcess.stop();
            }
            if (mockDatabase.mongodExecutable != null) {
                mockDatabase.mongodExecutable.stop();
            }
        }
    }

    private MongoMockDatabase createMockDatabase(String host, int port) throws IOException {
        // create the security database
        MongodStarter securitydbstarter = MongodStarter.getDefaultInstance();
        MongodExecutable mongodExecutable = securitydbstarter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V3_2)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build()
        );
        MongodProcess mongod = mongodExecutable.start();
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        return new MongoMockDatabase(mongodExecutable, mongod, mongoClient);
    }

    private void loadMockData(List<String> mockFiles) throws IOException {
        for (String mockFile : mockFiles) {
            String[] split = mockFile.split("\\.");
            DB database = mockDatabase.mongoClient.getDB(split[0]);

            Path filePath = Paths.get(".", "src", "test", "resources", "mock", mockFile);
            BufferedReader jsonReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
            StringBuilder json;
            for (json = new StringBuilder(); jsonReader.ready();
                 json.append(jsonReader.readLine())) {
            }

            BasicDBList mockData = (BasicDBList) JSON.parse(json.toString());
            WriteResult insert = database.createCollection(split[1], new BasicDBObject())
                    .insert(mockData.toArray(new BasicDBObject[mockData.size()]), database.getWriteConcern());

        }
    }

    private void createMockUser(String database, String user, String password) {
        DB db = mockDatabase.mongoClient.getDB(database);
        db.addUser(user, password.toCharArray());
    }

    private class MongoMockDatabase {

        public final MongodExecutable mongodExecutable;
        public final MongodProcess mongodProcess;
        public final MongoClient mongoClient;

        public MongoMockDatabase(MongodExecutable mongodExecutable, MongodProcess mongodProcess, MongoClient mongoClient) {
            this.mongodExecutable = mongodExecutable;
            this.mongodProcess = mongodProcess;
            this.mongoClient = mongoClient;
        }
    }
}
