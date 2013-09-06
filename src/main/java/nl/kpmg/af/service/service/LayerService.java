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
import nl.kpmg.af.service.response.assembler.EventAssembler;
import nl.kpmg.af.service.response.dto.EventDto;

/**
 * @author janos4276
 *
 */
/**
 * This class represents the layer rest service. Right now it's a Java re-write
 * of the current middleware layer service.
 *
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 */
@Path("layer")
public class LayerService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LayerService.class);
    private final EventDao eventDao;

    public LayerService() {
        eventDao = new EventDao(MongoDBUtil.getMongoDatabase());
    }

    /**
     * Get the corresponding json for the "n" layer.
     *
     * @param layer the layer
     * @return a list
     */
    @GET
    @Path("{layer}")
    @Produces("application/json")
    public Response get(@PathParam("layer") String layer) {
        List<Event> res;
        try {
            res = eventDao.fetchAll(layer);
        } catch (DataModelException e) {
            LOGGER.error("Request can not be processed, error has occured", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(res).build();
    }

    /**
     * Get the corresponding json for the "n" layer.
     *
     * @param n the layer
     * @return a list
     */
    @POST
    @Path("{layer}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("layer") String layer, LayerRequest request) {
        List<EventDto> result;
        try {
            List<Event> fetchedEvents = eventDao.fetchByFilter(
                    layer,
                    request.createMongoQuery(),
                    request.getLimit(),
                    request.createMongoOrder());
            result = EventAssembler.disassemble(fetchedEvents);
        } catch (DataModelException e) {
            LOGGER.error("Request can not be processed, error has occured", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(result).build();
    }
}
