package nl.kpmg.af.service.request.filter.location;

import java.util.List;

/**
 *
 * @author Hoekstra.Maarten
 */
public class GeoJSONPolygon extends GeoJSONPosition {
    /**
     * Because JBOSS wants this...
     */
    private List<List<List<Double>>> coordinates;

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
