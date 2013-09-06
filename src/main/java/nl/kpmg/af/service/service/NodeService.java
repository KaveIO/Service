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

import nl.kpmg.af.datamodel.dao.NodeDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Node;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.NodeRequest;
import nl.kpmg.af.service.response.assembler.NodeAssembler;
import nl.kpmg.af.service.response.dto.NodeDto;

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
@Path("nodes")
public class NodeService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NodeService.class);
    private final NodeDao nodeDao;

    public NodeService() {
        nodeDao = new NodeDao(MongoDBUtil.getMongoDatabase());
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
        List<NodeDto> result;
        try {
            List<Node> fetchedNodes = nodeDao.fetchAll(collection);
            result = NodeAssembler.disassemble(fetchedNodes);
        } catch (DataModelException ex) {
            LOGGER.error("Error has occured. Data could not be fetched.", ex);
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
    public Response post(@PathParam("collection") String collection, NodeRequest request) {
        List<NodeDto> result;
        try {
            List<Node> fetchedNodes = nodeDao.fetchByFilter(
                    collection,
                    request.createMongoQuery(),
                    request.getLimit());
            result = NodeAssembler.disassemble(fetchedNodes);
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
