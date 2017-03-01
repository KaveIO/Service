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

package nl.kpmg.af.service.data.core.repository;

import nl.kpmg.af.service.data.core.Measurement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.LinkedList;
import java.util.List;

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
      int pageCutoff =
          remainingObjects > pageable.getPageSize() ? pageable.getPageSize() : remainingObjects;

      if (pageCutoff == 0) {
        return new PageImpl(new LinkedList());
      }

      adhocQuery.limit(pageCutoff);
    }

    List<Measurement> find = mongoTemplate.find(adhocQuery, Measurement.class, collectionName);
    return new PageImpl(find);
  }

  @Override
  public List<Measurement> findPageless(Query adHocQuery) {
    return mongoTemplate.find(adHocQuery, Measurement.class, collectionName);
  }
}
