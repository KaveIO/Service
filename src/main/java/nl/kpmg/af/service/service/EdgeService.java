package nl.kpmg.af.service.service;

import nl.kpmg.af.service.MongoDBUtil;
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

import nl.kpmg.af.datamodel.dao.EdgeDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Edge;
import nl.kpmg.af.service.request.EdgeRequest;
import nl.kpmg.af.service.response.assembler.EdgeAssembler;
import nl.kpmg.af.service.response.dto.EdgeDto;

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
@Path("edges")
public class EdgeService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EdgeService.class);
    private final EdgeDao edgeDao;

    public EdgeService() {
        edgeDao = new EdgeDao(MongoDBUtil.getMongoDatabase());
    }

    /**
     * Get the corresponding json for the "n" layer.
     *
     * @param layer the layer
     * @return a list
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("collection") String collection) {
        List<EdgeDto> result;
        try {
            List<Edge> fetchedEvents = edgeDao.fetchAll(collection);
            result = EdgeAssembler.disassemble(fetchedEvents);
        } catch (DataModelException e) {
            LOGGER.error("Request can not be processed, error has occured", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(result).build();
    }

    /**
     * Get the corresponding json for the "n" layer.
     *
     * @param n the layer
     * @return a list
     */
    @POST
    @Path("{collection}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("collection") String collection, EdgeRequest request) {
        List<EdgeDto> result;
        try {
            List<Edge> fetchedEdges = edgeDao.fetchByFilter(
                    collection,
                    request.createMongoQuery(),
                    request.getLimit());
            result = EdgeAssembler.disassemble(fetchedEdges);
        } catch (DataModelException e) {
            LOGGER.error("Request can not be processed, error has occured", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(result).build();
    }
}
