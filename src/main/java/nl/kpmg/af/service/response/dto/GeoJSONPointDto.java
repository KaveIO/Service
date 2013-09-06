package nl.kpmg.af.service.response.dto;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Hoekstra.Maarten
 */
public final class GeoJSONPointDto extends GeoJSONPositionDto {
    /**
     * A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    private List<Double> coordinates = new LinkedList();

    public GeoJSONPointDto() {
        super("Point");
    }

    public GeoJSONPointDto(Double longitude, Double latitude) {
        super("Point");
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
