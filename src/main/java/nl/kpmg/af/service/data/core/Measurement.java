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

package nl.kpmg.af.service.data.core;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mhoekstra
 */
@Document
public class Measurement implements Map<String, Object> {

  @Id
  @Field(value = "_id")
  private ObjectId id;

  private Integer version;

  private Date measurementTimestamp;

  @Transient
  private final Map<String, Object> innerMap = new HashMap<>();

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Date getMeasurementTimestamp() {
    return measurementTimestamp;
  }

  public void setMeasurementTimestamp(Date measurementTimestamp) {
    this.measurementTimestamp = measurementTimestamp;
  }

  public Map<String, Object> getValue() {
    if (innerMap.containsKey("value")) {
      return (Map<String, Object>) innerMap.get("value");
    }
    return null;
  }

  public void setValue(Map<String, Object> value) {
    innerMap.put("value", value);
  }

  @Override
  public int size() {
    return innerMap.size();
  }

  @Override
  public boolean isEmpty() {
    return innerMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return innerMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return innerMap.containsValue(value);
  }

  @Override
  public Object get(Object key) {
    return innerMap.get(key);
  }

  @Override
  public Object put(String key, Object arg1) {
    return innerMap.put(key, arg1);
  }

  @Override
  public Object remove(Object key) {
    return innerMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ?> m) {
    innerMap.putAll(m);
  }

  @Override
  public void clear() {
    innerMap.clear();
  }

  @Override
  public Set keySet() {
    return innerMap.keySet();
  }

  @Override
  public Collection values() {
    return innerMap.values();
  }

  @Override
  public Set entrySet() {
    return innerMap.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return innerMap.equals(o);
  }

  @Override
  public int hashCode() {
    return innerMap.hashCode();
  }
}
