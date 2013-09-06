package nl.kpmg.af.service.response.dto;

import java.util.List;

/**
 * @author Hoekstra.Maarten
 */
public final class GeoJSONPolygonDto extends GeoJSONPositionDto {
    /**
     * A list of polygons used to define the GeoJSON polygon.
     */
    private List<List<List<Double>>> coordinates;

    /**
     * Default constructor.
     */
    public GeoJSONPolygonDto() {
        super("Polygon");
    }

    /**
     * @return A list of polygons used to define the GeoJSON polygon.
     */
    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates A list of polygons used to define the GeoJSON polygon.
     */
    public void setCoordinates(final List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
