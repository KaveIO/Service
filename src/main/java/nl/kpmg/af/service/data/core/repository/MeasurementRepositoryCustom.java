/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import nl.kpmg.af.service.data.core.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
/**
 *
 * @author mhoekstra
 */
public interface MeasurementRepositoryCustom {

    public Page<Measurement> find(Query adhocQuery, int limit, Pageable pageable);

}
