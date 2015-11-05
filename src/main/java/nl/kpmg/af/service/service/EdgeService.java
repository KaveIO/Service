package nl.kpmg.af.service.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.kpmg.af.datamodel.dao.EdgeDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Edge;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.EdgeRequest;
import nl.kpmg.af.service.response.assembler.EdgeAssembler;
import nl.kpmg.af.service.response.dto.EdgeDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents the edges rest service.
 * Right now it's a Java re-write of the current middleware layer service.
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 *
 * @author janos4276
 */
@Service
@Path("{applicationId}/edges")
public final class EdgeService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EdgeService.class);
    
    @Autowired
    private MongoDBUtil mongoDBUtil;

    /**
     * Get the corresponding json for the "collection" collection.
     *
     * @param applicationId The application ID.
     * @param collection the collection of edges to fetch from
     * @return the list of edges
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("applicationId") final String applicationId,
                        @PathParam("collection") final String collection) {
        try {
            EdgeDao edgeDao = mongoDBUtil.getDao(applicationId, EdgeDao.class);
            List<Edge> fetchedEdges = edgeDao.fetchAll(collection);
            List<EdgeDto> result = EdgeAssembler.disassemble(fetchedEdges);
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
     * @param collection the collection of edges to fetch from
     * @param request the request which determines which edges to return.
     * @return a list of edges
     */
    @POST
    @Path("{collection}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response post(@PathParam("applicationId") final String applicationId,
                         @PathParam("collection") final String collection, final EdgeRequest request) {
        try {
            EdgeDao edgeDao = mongoDBUtil.getDao(applicationId, EdgeDao.class);
            List<Edge> fetchedEdges = edgeDao.fetchByFilter(collection, request.createMongoQuery(), request.getLimit());
            List<EdgeDto> result = EdgeAssembler.disassemble(fetchedEdges);
            return Response.ok(result).build();
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
    }
    public MongoDBUtil getMongoDBUtil() {
        return mongoDBUtil;
    }
    @Autowired
    public void setMongoDBUtil(MongoDBUtil mongoDBUtil) {
        LOGGER.info("in EdgeService setting the MongoDBUtil bean.");
        this.mongoDBUtil = mongoDBUtil;        
    }
}
