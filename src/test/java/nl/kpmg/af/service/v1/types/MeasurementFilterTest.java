/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nl.kpmg.af.service.data.DatabaseInitialiser;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
public class MeasurementFilterTest {

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
    public void testGetQueryEmptyWhenNull() throws ApplicationDatabaseConnectionException, QueryCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();
        Query query = measurementFilter.getQuery();

        assertEquals(new Query(), query);
    }

    @Test(expected = QueryCastException.class)
    public void testGetQueryThrowsWhitNonSringContent() throws ApplicationDatabaseConnectionException, QueryCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();
        measurementFilter.put("query", new Integer(1));
        Query query = measurementFilter.getQuery();
    }

    @Test
    public void testGetQueryParsesContent() throws ApplicationDatabaseConnectionException, QueryCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();
        measurementFilter.put("query", "{version: 2}");
        Query query = measurementFilter.getQuery();

        DBObject queryObject = query.getQueryObject();

        assertTrue(queryObject.containsField("version"));
        assertTrue(queryObject.get("version").equals(2));
    }

    public void testGetQueryAvoidsNoSQLInjetion() {
        MeasurementFilter measurementFilter = new MeasurementFilter();
        measurementFilter.put("query", "{ \"$where\"  : function() { return obj.credits - obj.debits < 0; } }");

        Query query;
        try {
            query = measurementFilter.getQuery();
            fail();
        } catch (QueryCastException ex) { }


        measurementFilter.put("query", "{ \"$where\"  : \"function() { return obj.credits - obj.debits < 0; } }\"");
        try {
            query = measurementFilter.getQuery();
            fail();
        } catch (QueryCastException ex) { }
    }

    @Test
    public void testGetSortDefaultMeasuermentTimestampSortWhenNull() throws SortCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();
        Sort sort = measurementFilter.getSort();

        Iterator<Sort.Order> iterator = sort.iterator();
        Sort.Order next;

        next = iterator.next();
        assertEquals("measurementTimestamp", next.getProperty());
        assertEquals(Direction.DESC, next.getDirection());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSortParsesSortStatement() throws SortCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();

        List<Map> sortStatement = new LinkedList();

        Map<String, Integer> sortClause1 = new HashMap();
        sortClause1.put("fieldA", 1);

        Map<String, Integer> sortClause2 = new HashMap();
        sortClause2.put("fieldB", 0);

        sortStatement.add(sortClause1);
        sortStatement.add(sortClause2);

        measurementFilter.put("sort", sortStatement);

        Sort sort = measurementFilter.getSort();

        Iterator<Sort.Order> iterator = sort.iterator();
        Sort.Order next;

        next = iterator.next();
        assertEquals("fieldA", next.getProperty());
        assertEquals(Direction.ASC, next.getDirection());

        next = iterator.next();
        assertEquals("fieldB", next.getProperty());
        assertEquals(Direction.DESC, next.getDirection());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = SortCastException.class)
    public void testGetSortThrowsOnDoubleClause() throws SortCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();

        List<Map> sortStatement = new LinkedList();

        Map<String, Integer> doubleSortClause = new HashMap();
        doubleSortClause.put("fieldA", 1);
        doubleSortClause.put("fieldB", 0);

        sortStatement.add(doubleSortClause);

        measurementFilter.put("sort", sortStatement);

        measurementFilter.getSort();
    }

    @Test(expected = SortCastException.class)
    public void testGetSortThrowsOnDupplicateFieldClauses() throws SortCastException {
        MeasurementFilter measurementFilter = new MeasurementFilter();

        List<Map> sortStatement = new LinkedList();

        Map<String, Integer> sortClause1 = new HashMap();
        sortClause1.put("duplicateField", 1);

        Map<String, Integer> sortClause2 = new HashMap();
        sortClause2.put("duplicateField", 0);

        sortStatement.add(sortClause1);
        sortStatement.add(sortClause2);

        measurementFilter.put("sort", sortStatement);

        measurementFilter.getSort();
    }
}
