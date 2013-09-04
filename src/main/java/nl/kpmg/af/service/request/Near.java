package nl.kpmg.af.service.request;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;

/**
 *
 * @author Hoekstra.Maarten
 */
public class Near {
    private GeoJSONPoint geometry;
    private Double distance;

    public GeoJSONPoint getGeometry() {
        return geometry;
    }

    public void setGeometry(GeoJSONPoint geometry) {
        this.geometry = geometry;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

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
     * @return
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

        DBObject query = new BasicDBObject("$nearSphere", nearSphereArguments);
        return query;
    }
}
