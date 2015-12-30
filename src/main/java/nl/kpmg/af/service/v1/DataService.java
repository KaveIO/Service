/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author mhoekstra
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
     * @param applicationId The application ID.
     * @param collection the collection of edges to fetch from
     * @return
     */
    @GET
    @Produces("application/json")
    public Response getAll(
            @Context final UriInfo uriInfo,
            @PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection,
            @QueryParam("pageNumber") @DefaultValue("0") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("1000") final int pageSize) {

        try {
            validatePagination(pageNumber, pageSize);

            MeasurementRepository repository = mongoDBUtil.getRepository(applicationId, collection);

            Page<Measurement> page = repository.findAll(new PageRequest(pageNumber, pageSize));
            List<Measurement> content = page.getContent();

            Map<String, Link> links = generatePaginationLinks(uriInfo.getRequestUriBuilder(), page.getNumber(), page.getTotalPages());

            return Response.ok(new MeasurementsRepresentation(content, links)).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (PaginationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
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

    private static class PaginationException extends Exception {

        public PaginationException() {
        }

        private PaginationException(String message) {
            super(message);
        }
    }
}
