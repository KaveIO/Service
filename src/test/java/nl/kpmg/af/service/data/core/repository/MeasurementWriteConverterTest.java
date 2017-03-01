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

package nl.kpmg.af.service.data.core.repository;

import static org.junit.Assert.assertEquals;

import com.mongodb.DBObject;

import nl.kpmg.af.service.data.DatabaseInitialiser;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Measurement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

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
