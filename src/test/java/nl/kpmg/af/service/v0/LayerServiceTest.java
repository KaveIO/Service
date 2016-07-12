/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v0;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.request.LayerRequest;
import nl.kpmg.af.service.request.filter.LayerFilter;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import nl.kpmg.af.service.request.filter.timestamp.Timestamp;
import nl.kpmg.af.service.response.dto.EventDto;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author anaskar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@DirtiesContext
public class LayerServiceTest {

    /**
     * The logger for this class.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LayerServiceTest.class);
    /**
     * Spring ApplicationContext being Auto wired here.
     */
    @Autowired
    private ApplicationContext applicationContext;

    private static DatabaseInitialiser databaseInitialiser;

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


    @BeforeClass
    public static void setUpClass() throws Exception {
        databaseInitialiser = new DatabaseInitialiser();
        databaseInitialiser.start();
    }


    @AfterClass
    public static void tearDownClass() {
        databaseInitialiser.stop();
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
}
