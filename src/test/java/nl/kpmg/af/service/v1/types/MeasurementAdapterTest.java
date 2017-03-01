/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package nl.kpmg.af.service.v1.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import nl.kpmg.af.service.data.core.Measurement;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
