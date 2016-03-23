/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import com.mongodb.DBObject;
import java.util.Date;
import java.util.Set;
import nl.kpmg.af.service.data.core.Measurement;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

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
