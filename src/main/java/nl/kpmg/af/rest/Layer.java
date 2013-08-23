package nl.kpmg.af.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.mongodb.DBObject;

import nl.kpmg.af.dao.LayerDao;

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
     * Get the corresponding json for the "n" layer.
     *
     * @param n the layer
     * @return a list
     */
    @GET
    @Path("{n}.json")
    @Produces("application/json")
    public List<DBObject> get(@PathParam("n") String n) {
        final LayerDao layerDao = new LayerDao();
        return layerDao.get(n);
    }
}
