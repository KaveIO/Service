package nl.kpmg.af.service.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.kpmg.af.datamodel.dao.InputDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.model.Input;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.request.InputRequest;
import nl.kpmg.af.service.response.assembler.InputAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hoekstra.Maarten
 */
@Path("{applicationId}/input")
public final class InputService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InputService.class);

    /**
     * Get the corresponding json for the "collection" collection.
     * 
     * @param applicationId The ID of the application.
     * @param collection the collection of events to fetch from.
     * @param request the request which determines which events to return.
     * @return a list of events
     */
    @POST
    @Path("{collection}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response post(@PathParam("applicationId") final String applicationId,
                         @PathParam("collection") final String collection, final InputRequest request) {
        try {
            InputDao inputDao = MongoDBUtil.getDao(applicationId, InputDao.class);
            Input input = InputAssembler.assemble(collection, request);
            inputDao.store(input);
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DataModelException ex) {
            LOGGER.error("Error has occured. Data could not be stored.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
    }
}
