/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import java.util.Date;
import java.util.List;
import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.DBObject;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import nl.kpmg.af.service.data.MongoDBUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author roel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@DirtiesContext
public class MeasurementWriteConverterTest {
    
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
    public void testConvert() {
        Measurement m = new Measurement();
        m.setMeasurementTimestamp(new Date());
        m.setVersion(2);
        
        MeasurementWriteConverter mwc = new MeasurementWriteConverter();
        DBObject dbObject = mwc.convert(m);
        
        assertEquals(dbObject.get("version"), 2);
        
    }
}
