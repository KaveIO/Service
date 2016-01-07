/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter.location;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
        final GeoJSONPolygon geom = this.getGeometry();
        DBObject geoWithinArguments = new BasicDBObject();
        geoWithinArguments.put("$geometry", new BasicDBObject(new HashMap() {
            {
                this.put("type", geom.getType());
                this.put("coordinates", geom.getCoordinates());
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
