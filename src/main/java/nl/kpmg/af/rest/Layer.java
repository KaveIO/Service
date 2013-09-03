package nl.kpmg.af.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.mongodb.DBObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kpmg.af.datamodel.dao.DaoFactory;
import nl.kpmg.af.datamodel.dao.EventDao;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.datamodel.dao.filter.Filter;
import nl.kpmg.af.datamodel.dao.filter.MongoOrder;
import nl.kpmg.af.datamodel.model.Event;

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
public class Layer {
    
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Layer.class);

    private EventDao eventDao = DaoFactory.getEventDao();

    /**
     * Get the corresponding json for the "n" layer.
     * 
     * @param layer the layer
     * @return a list
     */
    @GET
    @Path("{layer}")
    @Produces("application/json")
    public List<DBObject> get(@PathParam("layer") String layer) {

     //   final LayerDao layerDao = new LayerDao();
     //   return layerDao.get(layer, 0);
        return null;
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
    public Response post(@PathParam("layer") String layer, Request request) {
        
        List<Event> res = null;
        try {
                      
            res = eventDao.fetchByFilter(layer, request.getFilter(),
                    request.getLimit(), request.getMongoOrder());
        } catch (DataModelException e) {
            LOGGER.error("Request can not be processed, error has occured", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        return Response.ok(res).build();
    }
}
