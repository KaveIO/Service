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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Extension of the preexisting Link.JaxbAdapter which adds support for marshaling lists of Links.
 *
 * @author mhoekstra
 */
public class JaxbLinksAdapter extends XmlAdapter<Map<String, Link.JaxbLink>, Map<String, Link>> {

  Link.JaxbAdapter JaxbAdapter = new Link.JaxbAdapter();

  @Override
  public Map<String, Link> unmarshal(Map<String, Link.JaxbLink> values) throws Exception {
    Map<String, Link> result = new HashMap();
    for (Map.Entry<String, Link.JaxbLink> value : values.entrySet()) {
      result.put(value.getKey(), JaxbAdapter.unmarshal(value.getValue()));
    }
    return result;
  }

  @Override
  public Map<String, Link.JaxbLink> marshal(Map<String, Link> values) throws Exception {
    Map<String, Link.JaxbLink> result = new HashMap();
    for (Map.Entry<String, Link> value : values.entrySet()) {
      result.put(value.getKey(), JaxbAdapter.marshal(value.getValue()));
    }
    return result;
  }
}
