/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import nl.kpmg.af.service.data.core.Measurement;
import org.bson.types.ObjectId;

/**
 *
 * @author mhoekstra
 */
public class MeasurementAdapter extends XmlAdapter<Map, Measurement> {

    public MeasurementAdapter() {
    }

    @Override
    public Measurement unmarshal(Map v) throws Exception {
        return null;
    }

    @Override
    public Map marshal(Measurement value) throws Exception {
        Map result = new HashMap();

        for (Object key : value.keySet()) {
            result.put(key, value.get(key));
        }

        if (value.getId() != null) {
            result.put("id", value.getId());
        }
        if (value.getVersion() != null) {
            result.put("version", value.getVersion());
        }
        if (value.getMeasurementTimestamp() != null) {
            result.put("measurementTimestamp", value.getMeasurementTimestamp());
        }

        result = convertObjectIds(result);

        return result;
    }

    private Map convertObjectIds(Map value) {
        Map result = new HashMap();

        for (Map.Entry entry : (Set<Map.Entry>) value.entrySet()) {
            if (ObjectId.class.isAssignableFrom(entry.getValue().getClass())) {
                result.put(entry.getKey(), entry.getValue().toString());
            } else if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                result.put(entry.getKey(), convertObjectIds((Map) entry.getValue()));
            } else if (List.class.isAssignableFrom(entry.getValue().getClass())) {
                result.put(entry.getKey(), convertObjectIds((List) entry.getValue()));
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    private List convertObjectIds(List value) {
        List result = new LinkedList();

        for (Object entry : value) {
            if (ObjectId.class.isAssignableFrom(entry.getClass())) {
                result.add(entry.toString());
            } else if (Map.class.isAssignableFrom(entry.getClass())) {
                result.add(convertObjectIds((Map) entry));
            } else if (List.class.isAssignableFrom(entry.getClass())) {
                result.add(convertObjectIds((List) entry));
            } else {
                result.add(entry);
            }
        }

        return result;
    }
}
