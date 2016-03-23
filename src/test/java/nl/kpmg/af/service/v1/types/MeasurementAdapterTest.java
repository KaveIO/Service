/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nl.kpmg.af.service.data.core.Measurement;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 *
 * @author mhoekstra
 */
public class MeasurementAdapterTest {

    private MeasurementAdapter measurementAdapter;

    @Before
    public void setup() {
        measurementAdapter = new MeasurementAdapter();
    }

    @Test
    public void testMarshalEmpty() throws Exception {
        Measurement measurement = new Measurement();

        Map actual = measurementAdapter.marshal(measurement);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testMarshalIdAsString() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(new ObjectId());

        Map actual = measurementAdapter.marshal(measurement);

        assertNotNull(actual);
        assertTrue(actual.containsKey("id"));
        assertTrue(String.class.isAssignableFrom(actual.get("id").getClass()));
    }

    @Test
    public void testMarshalObjectIdsAsString() throws Exception {
        Measurement measurement = new Measurement();

        measurement.setId(new ObjectId());
        measurement.put("nodeId", new ObjectId());
        measurement.put("value", new HashMap());
        ((Map) measurement.get("value")).put("valueId", new ObjectId());
        ((Map) measurement.get("value")).put("innerValue", new HashMap());
        ((Map) ((Map) measurement.get("value")).get("innerValue")).put("innerValueId", new ObjectId());
        measurement.put("list", new LinkedList());
        ((List) measurement.get("list")).add("test");
        ((List) measurement.get("list")).add(new ObjectId());

        Map actual = measurementAdapter.marshal(measurement);

        assertNotNull(actual);
        assertEquals(String.class, actual.get("id").getClass());
        assertEquals(String.class, actual.get("nodeId").getClass());
        assertEquals(HashMap.class, actual.get("value").getClass());

        HashMap actualValue = (HashMap) actual.get("value");
        assertEquals(String.class, actualValue.get("valueId").getClass());
        assertEquals(HashMap.class, actualValue.get("innerValue").getClass());

        HashMap actualInnerValue = (HashMap) actualValue.get("innerValue");
        assertEquals(String.class, actualInnerValue.get("innerValueId").getClass());

        assertEquals(LinkedList.class, actual.get("list").getClass());

        LinkedList list = (LinkedList) actual.get("list");
        assertEquals(String.class, list.get(0).getClass());
        assertEquals(String.class, list.get(1).getClass());
    }

}
