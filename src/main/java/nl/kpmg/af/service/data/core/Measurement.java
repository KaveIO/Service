/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mhoekstra
 */
@Document
public class Measurement implements Map {

    private final Map innerMap = new HashMap();

    public ObjectId getId() {
        if (innerMap.containsKey("_id")) {
            return (ObjectId) innerMap.get("_id");
        }
        return null;
    }

    public Integer getVersion() {
        if (innerMap.containsKey("version")) {
            return (Integer) innerMap.get("version");
        }
        return null;
    }

    public void setVersion(Integer version) {
        innerMap.put("version", version);
    }

    public Map getValue() {
        if (innerMap.containsKey("value")) {
            return (Map) innerMap.get("value");
        }
        return null;
    }

    public void setValue(Map value) {
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
    public Object put(Object arg0, Object arg1) {
        return innerMap.put(arg0, arg1);
    }

    @Override
    public Object remove(Object key) {
        return innerMap.remove(key);
    }

    @Override
    public void putAll(Map m) {
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
