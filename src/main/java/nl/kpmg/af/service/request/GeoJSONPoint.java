package nl.kpmg.af.service.request;

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

    @Override
    public String toJSONString() {
        String coordListString = String.format("%f, %f", getLongiude(), getLatitude());
        String val = String.format(
                "{\"type\" : \"%s\" , \"coordinates\" : [%s] }", type, coordListString);
        return val;

    }
}
