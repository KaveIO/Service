package nl.kpmg.af.service.request.filter.location;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;

/**
 * @author Hoekstra.Maarten
 */
public final class Near {
    /**
     * Adjacency based location filter for the layer request.
     */
    private GeoJSONPoint geometry;
    /**
     * Adjacency distance to given geometry point in meters.
     */
    private Double distance;

    /**
     * $nearSphere, only gives the first 100 documents!!!!!
     *
     * From the manual:
     * db.<collection>.find( { <location field> :
     * { $nearSphere :
     * { $geometry :
     * { type : "Point" , coordinates : [ <longitude> , <latitude> ] } ,
     * $maxDistance : <distance in meters>
     * } } } )
     *
     * @return Near as a nearSphere mongo query
     * @throws DataModelException
     */
    public DBObject getMongoCondition() {
        double dist = getDistance().doubleValue();
        final GeoJSONPoint geom = getGeometry();

        DBObject nearSphereArguments = new BasicDBObject();
        nearSphereArguments.put("$geometry", new BasicDBObject(new HashMap() {
            {
                put("type", geom.getType());
                put("coordinates", geom.getCoordinates());
            }
        }));
        nearSphereArguments.put("$maxDistance", dist);

        return new BasicDBObject("$nearSphere", nearSphereArguments);
    }

    /**
     * @return Adjacency based location filter for the layer request.
     */
    public GeoJSONPoint getGeometry() {
        return geometry;
    }

    /**
     * @param geometry Adjacency based location filter for the layer request.
     */
    public void setGeometry(final GeoJSONPoint geometry) {
        this.geometry = geometry;
    }

    /**
     * @return Adjacency distance to given geometry point in meters.
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance Adjacency distance to given geometry point in meters.
     */
    public void setDistance(final Double distance) {
        this.distance = distance;
    }
}
