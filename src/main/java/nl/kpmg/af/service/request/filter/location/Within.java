package nl.kpmg.af.service.request.filter.location;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;

/**
 * @author Hoekstra.Maarten
 */
public final class Within {
    /**
     * Polygon for the location filter for the layer request.
     */
    private GeoJSONPolygon geometry;

    /**
     * From the website:
     * db.<collection>.find( { <location field> :
     * { $geoWithin :
     * { $geometry :
     * { type : "Polygon" ,
     * coordinates : [ [ [ <lng1>, <lat1> ] , [ <lng2>, <lat2> ] ... ] ]
     * } } } } )
     *
     * @return Within as a nearSphere mongo query
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
        return new BasicDBObject("$geoWithin", geoWithinArguments);
    }

    /**
     * @return Polygon for the location filter for the layer request.
     */
    public GeoJSONPolygon getGeometry() {
        return geometry;
    }

    /**
     * @param geometry Polygon for the location filter for the layer request.
     */
    public void setGeometry(final GeoJSONPolygon geometry) {
        this.geometry = geometry;
    }
}
