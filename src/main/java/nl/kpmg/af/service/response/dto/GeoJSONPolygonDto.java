package nl.kpmg.af.service.response.dto;

import java.util.List;

/**
 *
 * @author Hoekstra.Maarten
 */
public final class GeoJSONPolygonDto extends GeoJSONPositionDto {
    /**
     * Because JBOSS wants this...
     */
    private List<List<List<Double>>> coordinates;

    public GeoJSONPolygonDto() {
        super("Polygon");
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
