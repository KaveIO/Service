package nl.kpmg.af.service.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.kpmg.af.datamodel.dao.EventDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Event;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.LayerRequest;
import nl.kpmg.af.service.request.aggregation.Aggregation;
import nl.kpmg.af.service.request.aggregation.AggregationType;
import nl.kpmg.af.service.response.assembler.EventAssembler;
import nl.kpmg.af.service.response.dto.EventDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the layer rest service.
 * Right now it's a Java re-write of the current middleware layer service.
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 * 
 * @author janos4276
 */
@Path("{applicationId}/layer")
public final class LayerService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerService.class);

    /**
     * Get the corresponding json for the "collection" collection.
     * 
     * @param applicationId The application ID.
     * @param collection the collection of events to fetch from
     * @return the list of events
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("applicationId") final String applicationId,
                        @PathParam("collection") final String collection) {
        try {
            EventDao eventDao = MongoDBUtil.getDao(applicationId, EventDao.class);
            List<Event> fetchedEvents = eventDao.fetchAll(collection);
            List<EventDto> result = EventAssembler.disassemble(fetchedEvents);
            return Response.ok(result).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DataModelException ex) {
            LOGGER.error("Error has occured. Data could not be fetched.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get the corresponding json for the "collection" collection.
     * 
     * @param applicationId The application ID.
     * @param collection the collection of events to fetch from
     * @param request the request which determines which events to return.
     * @return a list of events
     */
    @POST
    @Path("{collection}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("applicationId") final String applicationId,
                         @PathParam("collection") final String collection, final LayerRequest request) {
        List<EventDto> result;
        try {
            EventDao eventDao = MongoDBUtil.getDao(applicationId, EventDao.class);
            Aggregation aggregation = request.getAggregation();
            if (aggregation == null) {
                List<Event> fetchedEvents = eventDao.fetchByFilter(collection, request.createMongoQuery(),
                                                                   request.getLimit(), request.createMongoOrder());
                result = EventAssembler.disassemble(fetchedEvents);
            } else if (aggregation.getType() == AggregationType.LATEST) {
                List<Event> fetchedEvents = eventDao.fetchLatestByFilter(collection, aggregation.getBy(),
                                                                         request.createMongoQuery());
                result = EventAssembler.disassemble(fetchedEvents);
            } else {
                LOGGER.warn("Error has occured. An unknown aggregation type has been requested.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InvalidRequestException ex) {
            LOGGER.warn("Error has occured. Request can not be processed.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DataModelException ex) {
            LOGGER.error("Error has occured. Data could not be fetched.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(result).build();
    }
}
