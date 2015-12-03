/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.kpmg.af.service.service;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.request.LayerRequest;
import nl.kpmg.af.service.request.filter.LayerFilter;
import nl.kpmg.af.service.request.filter.timestamp.DataLoadingTest;
import nl.kpmg.af.service.request.filter.timestamp.DatabaseInitialiser;
import nl.kpmg.af.service.request.filter.timestamp.Timestamp;
import nl.kpmg.af.service.response.dto.EventDto;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *l
 * @author anaskar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testapplicationContext.xml"})
public class LayerServiceTest extends DataLoadingTest {

    /**
     * The logger for this class.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LayerServiceTest.class);
    /**
     * Spring ApplicationContext being Auto wired here.
     */
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * The MongoDBUtil bean is also being AutoWired .
     */
    @Autowired
    private MongoDBUtil mongoDBUtil;

    // commented out,can later look at how to deal
    //    @Autowired
    //    public DatabaseInitialiser databaseInitialiser;
    /**
     * The declaration of the string collection and other constants .
     */
    public static final String COLLECTION = "visit";
    public static final String APPLICATION_ID = "test";
    public static final int RANGE_AFTER = 1434240000;
    public static final int RANGE_BEFORE = 1434412800;
    public static final int TEST_DATE_TIME_PAST_WINDOW = 100000000;

    public static final int UNIX_DATE_TIME_PAST_ONE = 1434412800;
    public static final int UNIX_DATE_TIME_PAST_TWO = 1434240000;
    public static final int UNIX_DATE_TIME_PAST_THREE = 1434326400;

    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongod;
    private static MongoClient mongoClient;

    public LayerServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        try {
            DatabaseInitialiser.startMongoLayerService();
        } catch (Exception ex) {
            if (mongoClient != null) {
                mongoClient.close();
            }
            if (mongod != null) {
                mongod.stop();
            }
            if (mongodExecutable != null) {
                mongodExecutable.stop();
            }
            Logger.getLogger(LayerServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {

        if (mongoClient != null) {
            mongoClient.close();
        }
        if (mongod != null) {
            mongod.stop();
        }
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }

    @After
    public void tearDown() {

    }

    /**
     * Test of the scenario where two dates are sent to mongoDB a greaterThan
     * date and a lesserThan Date ,the REST API call is made through the fetch
     * method of the EventDAO class and its checked if the dates retrieved fall
     * within the range
     */
    @Test
    public void testDateRange() {

        LayerRequest request = new LayerRequest();

        Timestamp timestamp = new Timestamp();
        timestamp.setAfter(RANGE_AFTER);
        timestamp.setBefore(RANGE_BEFORE);

        LayerFilter filter = new LayerFilter();
        filter.setTimestamp(timestamp);
        request.setFilter(filter);

        // declare a list to compare data retrieved from database        
        List<String> retrievedDatelist = new ArrayList<>();
        LayerService bean = applicationContext.getBean(LayerService.class);
        Response response = bean.post(APPLICATION_ID, COLLECTION, request);

        List<EventDto> responseList = (List<EventDto>) response.getEntity();
        for (EventDto next : responseList) {
            retrievedDatelist.add(next.getMeasurementTimestamp().toString());
        }
        long unixTimeFirst = 1434326400;

        List<String> expecteddatelist = new ArrayList<>();
        expecteddatelist.add(String.valueOf(unixTimeFirst));
        assertEquals(retrievedDatelist, expecteddatelist);

    }

    @Test
    public void testDatePastTimeWindow() {

        LayerRequest request = new LayerRequest();
        Timestamp timestamp = new Timestamp();
        timestamp.setPastwindow(TEST_DATE_TIME_PAST_WINDOW);
        LayerFilter filter = new LayerFilter();
        filter.setTimestamp(timestamp);
        request.setFilter(filter);

        // declare a list to compare data retrieved from database        
        List<String> retrievedDatelist = new ArrayList<>();

        LayerService bean = applicationContext.getBean(LayerService.class);
        Response response = bean.post(APPLICATION_ID, COLLECTION, request);

        System.out.println("type of RESPONSE entity" + response.getStatus());

        List<EventDto> responseList = (List<EventDto>) response.getEntity();
        for (EventDto next : responseList) {
            retrievedDatelist.add(next.getMeasurementTimestamp().toString());
        }

        List<String> expecteddatelist = new ArrayList<>();
        expecteddatelist.add(String.valueOf(UNIX_DATE_TIME_PAST_ONE));
        expecteddatelist.add(String.valueOf(UNIX_DATE_TIME_PAST_TWO));
        expecteddatelist.add(String.valueOf(UNIX_DATE_TIME_PAST_THREE));

        assertEquals(retrievedDatelist, expecteddatelist);

    }

    @Test
    public void testLessThanDate() throws ApplicationDatabaseConnectionException {

        LayerRequest request = new LayerRequest();
        Timestamp timestamp = new Timestamp();
        timestamp.setBefore(1434412800);

        LayerFilter filter = new LayerFilter();
        filter.setTimestamp(timestamp);
        request.setFilter(filter);
        List<String> retrievedDatelist = new ArrayList<>();

        LayerService bean = applicationContext.getBean(LayerService.class);
        Response response = bean.post(APPLICATION_ID, COLLECTION, request);

        List<EventDto> responseList = (List<EventDto>) response.getEntity();

        for (EventDto next : responseList) {
            retrievedDatelist.add(next.getMeasurementTimestamp().toString());
        }
        long unixTime3 = 1434240000;
        long unixTime4 = 1434326400;

        Date date3 = new Date("Sun Jun 14 00:00:00 UTC 2015");
        Date date4 = new Date("Mon Jun 15 00:00:00 UTC 2015");

        List<String> expecteddatelist = new ArrayList<String>();
        expecteddatelist.add(String.valueOf(unixTime3));
        expecteddatelist.add(String.valueOf(unixTime4));

        assertEquals(retrievedDatelist, expecteddatelist);

    }

    @Test
    public void testgreaterThanDate() {

        LayerRequest request = new LayerRequest();
        Timestamp timestamp = new Timestamp();

        timestamp.setAfter(1434326400);
        LayerFilter filter = new LayerFilter();
        filter.setTimestamp(timestamp);
        request.setFilter(filter);
        List<String> retrievedDatelist = new ArrayList<>();

        LayerService bean = applicationContext.getBean(LayerService.class);
        Response response = bean.post(APPLICATION_ID, COLLECTION, request);

        List<EventDto> responseList = (List<EventDto>) response.getEntity();

        for (EventDto next : responseList) {
            retrievedDatelist.add(next.getMeasurementTimestamp().toString());
        }
        long unixTime3 = 1434412800;
        List<String> expecteddatelist = new ArrayList<>();
        expecteddatelist.add(String.valueOf(unixTime3));

        boolean b = retrievedDatelist.equals(expecteddatelist);

        assertEquals(retrievedDatelist, expecteddatelist);

    }

    public MongoDBUtil getMongoDBUtil() {
        return mongoDBUtil;
    }

    @Autowired
    private void setMongoDBUtil(MongoDBUtil mongoDBUtil) {
        LOGGER.info("in NewLayerServiceTest setting the MongoDBUtil bean.");
        this.mongoDBUtil = mongoDBUtil;
    }
}
