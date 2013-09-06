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

    /**
     * Default constructor.
     */
    public GeoJSONPointDto() {
        super("Point");
    }

    /**
     * Directly sets latitude and longitude.
     *
     * @param longitude longitude for this GeoJSONPoint
     * @param latitude latitude for this GeoJSONPoint
     */
    public GeoJSONPointDto(final Double longitude, final Double latitude) {
        super("Point");
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    /**
     * @return A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    public void setCoordinates(final List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
