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

import nl.kpmg.af.service.data.core.Measurement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author mhoekstra
 */
public class MeasurementsAdapter extends XmlAdapter<List<Map>, List<Measurement>> {

  private final MeasurementAdapter measurementAdapter = new MeasurementAdapter();

  public MeasurementsAdapter() {}

  @Override
  public List<Measurement> unmarshal(List<Map> v) throws Exception {
    List<Measurement> result = new LinkedList();
    for (Map v1 : v) {
      result.add(measurementAdapter.unmarshal(v1));
    }
    return result;
  }

  @Override
  public List<Map> marshal(List<Measurement> v) throws Exception {
    List<Map> result = new LinkedList();
    for (Measurement v1 : v) {
      result.add(measurementAdapter.marshal(v1));
    }
    return result;
  }
}
