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

import com.mongodb.DBObject;

import nl.kpmg.af.service.data.core.Measurement;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author mhoekstra
 */
public class MeasurementReadConverter implements Converter<DBObject, Measurement> {

  @Override
  public Measurement convert(DBObject source) {
    Measurement measurement = new Measurement();

    Set<String> keySet = source.keySet();
    for (String key : keySet) {

      switch (key) {
        case "_id":
          measurement.setId((ObjectId) source.get(key));
          break;
        case "version":
          measurement.setVersion((Integer) source.get(key));
          break;
        case "measurementTimestamp":
          measurement.setMeasurementTimestamp((Date) source.get(key));
          break;
        default:
          measurement.put(key, source.get(key));
      }
    }
    return measurement;
  }
}
