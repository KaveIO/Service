/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import java.util.Date;
import java.util.List;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author mhoekstra
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@DirtiesContext
public class MeasurementRepositoryImplTest {

    private static DatabaseInitialiser databaseInitialiser;

    @Autowired
    private MongoDBUtil mongoDBUtil;

    @BeforeClass
    public static void setUpClass() throws Exception {
        databaseInitialiser = new DatabaseInitialiser();
        databaseInitialiser.start();
    }

    @AfterClass
    public static void tearDownClass() {
        databaseInitialiser.stop();
    }

    @Test
    public void testFindLimit() throws ApplicationDatabaseConnectionException {
        MeasurementRepository repository = mongoDBUtil.getRepository("test", "visitLayer");

        List<Measurement> all = repository.findAll();

        assertEquals(3, all.size());

        Page<Measurement> limited = repository.find(new Query(), 1, new PageRequest(0, 10));

        assertEquals(1, limited.getContent().size());
    }

    @Test
    public void testFindLimitMultiplePages() throws ApplicationDatabaseConnectionException {
        MeasurementRepository repository = mongoDBUtil.getRepository("test", "visitLayer");

        List<Measurement> all = repository.findAll();

        assertEquals(3, all.size());

        Page<Measurement> limited = repository.find(new Query(), 2, new PageRequest(0, 1));

        assertEquals(1, limited.getContent().size());

        limited = repository.find(new Query(), 2, new PageRequest(1, 1));

        assertEquals(1, limited.getContent().size());

        limited = repository.find(new Query(), 2, new PageRequest(2, 1));

        assertEquals(0, limited.getContent().size());
    }

    @Test
    public void testFindWithKnownFieldQuery() throws ApplicationDatabaseConnectionException {
        MeasurementRepository repository = mongoDBUtil.getRepository("test", "visitLayer");

        Page<Measurement> find;

        find = repository.find(new BasicQuery("{\"version\": 2}"), 0, new PageRequest(0, 1000));
        assertEquals(3, find.getTotalElements());

        find = repository.find(new BasicQuery("{\"measurementTimestamp\": { \"$gte\": { \"$date\": \"2015-06-14T00:00:00.00Z\" }}}"), 0, new PageRequest(0, 1000));
        assertEquals(3, find.getTotalElements());
    }

    @Test
    public void testSave() throws ApplicationDatabaseConnectionException {
        MeasurementRepository repository = mongoDBUtil.getRepository("test", "emptyLayer");
        Measurement measurement = new  Measurement();
        measurement.setVersion(2);
        measurement.setMeasurementTimestamp(new Date());

        repository.save(measurement);

        List<Measurement> ms = repository.findAll();

        assertEquals(1, ms.size());
        Measurement m = ms.get(0);
        assertEquals((Integer)2, m.getVersion());
    }
}
