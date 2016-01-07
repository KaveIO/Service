/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import nl.kpmg.af.service.data.core.Measurement;

/**
 *
 * @author mhoekstra
 */
public class MeasurementsAdapter extends XmlAdapter<List<Map>, List<Measurement>> {

    private final MeasurementAdapter measurementAdapter = new MeasurementAdapter();

    public MeasurementsAdapter() {
    }

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
