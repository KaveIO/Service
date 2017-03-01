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

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author mhoekstra
 */
public class MeasurementsRepresentation {

  @XmlElement(name = "items")
  @XmlJavaTypeAdapter(MeasurementsAdapter.class)
  private List<Measurement> items;

  @XmlElement(name = "links")
  @XmlJavaTypeAdapter(JaxbLinksAdapter.class)
  private Map<String, Link> links;

  public MeasurementsRepresentation(List<Measurement> items, Map<String, Link> links) {
    this.items = items;
    this.links = links;
  }

  public List<Measurement> getItems() {
    return items;
  }

  public void setItems(List<Measurement> items) {
    this.items = items;
  }

  public Map<String, Link> getLinks() {
    return links;
  }

  public void setLinks(Map<String, Link> links) {
    this.links = links;
  }

}
