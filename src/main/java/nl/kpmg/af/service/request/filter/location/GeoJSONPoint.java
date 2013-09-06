package nl.kpmg.af.service.request.filter.location;

import java.util.List;

/**
 *
 * @author Hoekstra.Maarten
 */
public class GeoJSONPoint extends GeoJSONPosition {
    /**
     * A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    private List<Double> coordinates;

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    double getLongiude() {
        return coordinates.get(0);
    }

    double getLatitude() {
        return coordinates.get(1);
    }
}
