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

package nl.kpmg.af.service.v1;

import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.data.core.repository.MeasurementRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.v1.types.MeasurementsRepresentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;


/**
 * A service that allows for arbitrary MongoDB queries that can be used in a "find" request.
 *
 * Usage: $ curl {https, user, password, host, prefix if on
 * JBoss}/v1/dataQuery/{applicationId}/{collection} -d '{"foo": "bar"}' -H "Content-Type:
 * text/plain"
 *
 * We aim to keep the queries generic, as in the above "foo": "bar", so without MongoDB-specific
 * operators $gt, $lt etc. to avoid tightly coupling any clients to the MongoDB query language.
 *
 * @author jvlier
 */
@Service
@Path("v1/dataQuery/{applicationId}/{collection}")
public class DataQueryService {

  /**
   * The logger for this class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataQueryService.class);

  @Autowired
  private MongoDBUtil mongoDBUtil;

  /**
   * Method for retrieving data for a specific collection with an arbitrary find request.
   *
   * This service currently does *not* use paging, because it's not needed for the use case for
   * which it was developed initially (LAS requests on aggregated data, around 1 to 100 documents
   * maximum in typical queries). So it didn't seem worthwhile to burden this service with the
   * additional complexity of pagination. Feel free to add it if needed though.
   *
   * @param applicationId The application ID
   * @param collection the collection of edges to fetch from
   * @param limit the maximum amount of records to return
   * @param sortDirection direction in which the measurements are sorted
   * @param sortField field name on which the measurements are sorted
   * @param data string of a MongoDB query, something that you would otherwise put in find()
   *
   * @return A list of measurements
   */
  @POST
  @Consumes("text/plain")
  @Produces("application/json")
  public Response getData(@PathParam("applicationId") final String applicationId,
      @PathParam("collection") final String collection,
      @QueryParam("limit") @DefaultValue("1000") final int limit,
      @QueryParam("sortDirection") @DefaultValue("DESC") final Sort.Direction sortDirection,
      @QueryParam("sortField") @DefaultValue("measurementTimestamp") final String sortField,
      final String data) {

    try {
      final Query query = new BasicQuery(data);
      query.with(new Sort(new Sort.Order(sortDirection, sortField)));
      query.limit(limit);

      final MeasurementRepository repository = mongoDBUtil.getRepository(applicationId, collection);
      final List<Measurement> content = repository.findPageless(query);

      return Response.ok(new MeasurementsRepresentation(content, new HashMap<String, Link>()))
          .build();
    } catch (ApplicationDatabaseConnectionException ex) {
      LOGGER.error("Error has occurred. The application database could not be connected.", ex);
      return Response.serverError().build();
    }
  }
}
