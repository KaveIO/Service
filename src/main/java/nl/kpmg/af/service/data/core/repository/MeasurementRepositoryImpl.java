/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import java.util.LinkedList;
import java.util.List;
import nl.kpmg.af.service.data.core.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author mhoekstra
 */
public class MeasurementRepositoryImpl implements MeasurementRepositoryCustom {

    private final String collectionName;

    private final MongoTemplate mongoTemplate;

    public MeasurementRepositoryImpl(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
    }

    @Override
    public Page<Measurement> find(Query adhocQuery, int limit, Pageable pageable) {
        adhocQuery.with(pageable);

        if (limit > 0) {
            int remainingObjects = limit - (pageable.getPageNumber() * pageable.getPageSize());
            remainingObjects = remainingObjects < 0 ? 0 : remainingObjects;
            int pageCutoff = remainingObjects > pageable.getPageSize()?pageable.getPageSize():remainingObjects;

            if (pageCutoff == 0) {
                return new PageImpl(new LinkedList());
            }

            adhocQuery.limit(pageCutoff);
        }

        List<Measurement> find = mongoTemplate.find(adhocQuery, Measurement.class, collectionName);
        return new PageImpl(find);
    }
}
