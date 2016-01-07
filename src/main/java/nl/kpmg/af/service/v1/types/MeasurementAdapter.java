/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import nl.kpmg.af.service.data.core.Measurement;

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
            if (key.equals("_id")) {
                result.put("id", value.get(key).toString());
            } else {
                result.put(key, value.get(key));
            }
        }

        if (value.getVersion() != null) result.put("version", value.getVersion());
        if (value.getVersion() != null) result.put("measurementTimestamp", value.getMeasurementTimestamp());

        return result;
    }
}
