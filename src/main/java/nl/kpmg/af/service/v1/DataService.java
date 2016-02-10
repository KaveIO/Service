/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1;

import nl.kpmg.af.service.v1.types.PaginationException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.data.core.repository.MeasurementRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.v1.types.MeasurementsRepresentation;
import nl.kpmg.af.service.v1.types.QueryCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author mhoekstra
 * @author roel
 */
@Service
@Path("v1/data/{applicationId}/{collection}")
public class DataService {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private MongoDBUtil mongoDBUtil;

    /**
     * Method for retrieving data for a specific collection.
     *
     * @param uriInfo Context information used for generating pagination requests
     * @param applicationId The application ID
     * @param collection the collection of edges to fetch from
     * @param pageNumber the index number of the page being requested
     * @param pageSize the amount of records within this page to retrieve
     * @param limit the maximum amount of records to return spanning the paginated requests
     * @param sortDirection direction in which the measurements are sorted
     * @param sortField field name on which the measurements are sorted
     *
     * @return A list of measurements wrapped with pagination links
     */
    @GET
    @Produces("application/json")
    public Response getData(
            @Context final UriInfo uriInfo,
            @PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection,
            @QueryParam("pageNumber") @DefaultValue("0") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("1000") final int pageSize,
            @QueryParam("limit") @DefaultValue("-1") final int limit,
            @QueryParam("sortDirection") @DefaultValue("DESC") final Sort.Direction sortDirection,
            @QueryParam("sortField") @DefaultValue("measurementTimestamp") final String sortField) {

        try {
            validatePagination(pageNumber, pageSize);

            MeasurementRepository repository = mongoDBUtil.getRepository(applicationId, collection);
            Page<Measurement> page = repository.findAll(
                    new PageRequest(
                            pageNumber,
                            pageSize,
                            sortDirection,
                            sortField));
            List<Measurement> content = page.getContent();
            Map<String, Link> links = generatePaginationLinks(uriInfo.getRequestUriBuilder(), page.getNumber(), page.getTotalPages());
            return Response.ok(new MeasurementsRepresentation(content, links)).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (PaginationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Problem detected in pagination.").build();
        }
    }

    /**
     * Method for storing new data into a specific collection.
     *
     *
     * @note Currently custom unmarshalling is used for input interpretation. This could be replaced by something more
     *   maintainable.
     *
     * @param applicationId The application ID
     * @param collection the collection of edges to fetch from
     * @param data a list of measurements items
     * @return Status code 200 on success
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response postData(
            @PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection,
            List data) {
        try {
            MeasurementRepository repository = mongoDBUtil.getRepository(applicationId, collection);
            List<Measurement> measurements = constructMeasurements(data);
            repository.save(measurements);
            return Response.status(Response.Status.OK).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (QueryCastException ex) {
            LOGGER.warn("Erroneous query content detected", ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("Problem detected in query request, this incident is logged.").build();
        }
    }

    private void validatePagination(int pageNumber, int pageSize) throws PaginationException {
        if (pageNumber < 0) {
            throw new PaginationException("Page number can't be smaller that 1");
        }
        if (pageSize < 1) {
            throw new PaginationException("Page size can't be smaller that 1");
        }
    }

    private Map<String, Link> generatePaginationLinks(UriBuilder baseUriBuilder, int currentPageNumber, int totalPages) {
        Map<String, Link> links = new HashMap();
        if (currentPageNumber + 1 < totalPages) {
            UriBuilder next = baseUriBuilder.clone();
            URI nextUri = next.replaceQueryParam("pageNumber", currentPageNumber + 1).build();
            Link nextLink = Link.fromUri(nextUri).build();
            links.put("next", nextLink);
        }

        if (currentPageNumber > 0) {
            UriBuilder previous = baseUriBuilder.clone();
            URI previousUri = previous.replaceQueryParam("pageNumber", currentPageNumber - 1).build();
            Link previousLink = Link.fromUri(previousUri).build();
            links.put("previous", previousLink);
        }
        return links;
    }

    private List<Measurement> constructMeasurements(List data) throws QueryCastException {
        List<Measurement> measurements = new ArrayList<Measurement>();
        for (Object record : data) {
            if (Map.class.isAssignableFrom(record.getClass())) {
                Measurement m = constructMeasurement((Map) record);
                measurements.add(m);
            } else {
                throw new QueryCastException();
            }
        }
        return measurements;
    }

    private Measurement constructMeasurement(Map record) {
        Measurement m = new Measurement();
        m.setMeasurementTimestamp(new Date());
        m.setVersion(2);
        m.putAll(record);
        return m;
    }
}
