/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import com.mongodb.DBObject;
import nl.kpmg.af.service.data.core.Measurement;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author mhoekstra
 */
public class MeasurementWriteConverter implements Converter<Measurement, DBObject> {

    @Override
    public DBObject convert(Measurement s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
