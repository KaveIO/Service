package nl.kpmg.af.service.request;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;

/**
 *
 * @author Hoekstra.Maarten
 */
public class Within {
    private GeoJSONPolygon geometry;

    public Within() {
    }

    public GeoJSONPolygon getGeometry() {
        return geometry;
    }

    public void setGeometry(GeoJSONPolygon geo) {
        geometry = geo;
    }

    /**
     * From the website:
     * db.<collection>.find( { <location field> :
     * { $geoWithin :
     * { $geometry :
     * { type : "Polygon" ,
     * coordinates : [ [ [ <lng1>, <lat1> ] , [ <lng2>, <lat2> ] ... ] ]
     * } } } } )
     *
     * @return
     */
    public DBObject getMongoCondition() {
        final GeoJSONPolygon geom = getGeometry();
        DBObject geoWithinArguments = new BasicDBObject();
        geoWithinArguments.put("$geometry", new BasicDBObject(new HashMap() {
            {
                put("type", geom.getType());
                put("coordinates", geom.getCoordinates());
            }
        }));
        DBObject nearSphere = new BasicDBObject("$geoWithin", geoWithinArguments);
        DBObject query = new BasicDBObject("location", nearSphere);
        return query;
    }
}
