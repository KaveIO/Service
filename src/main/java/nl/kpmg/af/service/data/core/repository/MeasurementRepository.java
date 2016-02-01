/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import java.util.List;
import nl.kpmg.af.service.data.core.Measurement;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author mhoekstra
 */
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Long>, MeasurementRepositoryCustom {

    public List<Measurement> findAll();

    public List<Measurement> findByVersion(int version);

}
