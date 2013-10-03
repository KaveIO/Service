package nl.kpmg.af.service.service;

import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.request.LayerRequest;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kpmg.af.datamodel.dao.EventDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Event;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.aggregation.Aggregation;
import nl.kpmg.af.service.request.aggregation.AggregationType;
import nl.kpmg.af.service.response.assembler.EventAssembler;
import nl.kpmg.af.service.response.dto.EventDto;

/**
 * This class represents the layer rest service.
 * Right now it's a Java re-write of the current middleware layer service.
 *
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 *
 * @author janos4276
 */
@Path("layer")
public final class LayerService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerService.class);
    /**
     * DAO object used for fetching events.
     */
    private final EventDao eventDao;

    /**
     * Default constructor fetches the DAO from MongoDBUtil.
     */
    public LayerService() {
        eventDao = new EventDao(MongoDBUtil.getMongoDatabase());
    }

    /**
     * Get the corresponding json for the "collection" collection.
     *
     * @param collection the collection of events to fetch from
     * @return the list of events
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("collection") final String collection) {
        List<EventDto> result;
        try {
            List<Event> fetchedEvents = eventDao.fetchAll(collection);
            result = EventAssembler.disassemble(fetchedEvents);
        } catch (DataModelException ex) {
            LOGGER.error("Error has occured. Data could not be fetched.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(result).build();
    }

    /**
     * Get the corresponding json for the "collection" collection.
     *
     * @param collection the collection of events to fetch from
     * @param request the request which determines which events to return.
     * @return a list of events
     */
    @POST
    @Path("{collection}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("collection") final String collection, final LayerRequest request) {
        List<EventDto> result;
        try {
            Aggregation aggregation = request.getAggregation();
            if (aggregation == null) {
                List<Event> fetchedEvents = eventDao.fetchByFilter(
                        collection,
                        request.createMongoQuery(),
                        request.getLimit(),
                        request.createMongoOrder());
                result = EventAssembler.disassemble(fetchedEvents);
            } else if (aggregation.getType() == AggregationType.LATEST) {
                List<Event> fetchedEvents = eventDao.fetchLatestByFilter(
                        collection,
                        aggregation.getBy(),
                        request.createMongoQuery());
                result = EventAssembler.disassemble(fetchedEvents);
            } else {
                LOGGER.warn("Error has occured. An unknown aggregation type has been requested.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
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
