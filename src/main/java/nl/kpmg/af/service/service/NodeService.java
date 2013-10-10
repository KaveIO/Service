package nl.kpmg.af.service.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.kpmg.af.datamodel.dao.NodeDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Node;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.NodeRequest;
import nl.kpmg.af.service.response.assembler.NodeAssembler;
import nl.kpmg.af.service.response.dto.NodeDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the nodes rest service.
 * Right now it's a Java re-write of the current middleware layer service.
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 * 
 * @author janos4276
 */
@Path("nodes")
public final class NodeService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);
    /**
     * DAO object used for fetching nodes.
     */
    private final NodeDao nodeDao;

    /**
     * Default constructor fetches the DAO from MongoDBUtil.
     */
    public NodeService() {
        nodeDao = new NodeDao(MongoDBUtil.getMongoDatabase());
    }

    /**
     * Get the corresponding json for the "collection" collection.
     * 
     * @param collection the collection of nodes to fetch from
     * @return the list of nodes
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("collection") final String collection) {
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
     * Get the corresponding json for the "collection" collection.
     * 
     * @param collection the collection of nodes to fetch from
     * @param request the request which determines which nodes to return.
     * @return a list of nodes
     */
    @POST
    @Path("{collection}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("collection") final String collection, final NodeRequest request) {
        List<NodeDto> result;
        try {
            List<Node> fetchedNodes = nodeDao.fetchByFilter(collection, request.createMongoQuery(), request.getLimit());
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
