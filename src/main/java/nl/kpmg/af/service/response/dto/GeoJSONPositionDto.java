package nl.kpmg.af.service.response.dto;

public abstract class GeoJSONPositionDto {
    /**
     * The type of the GeoJSON item
     */
    protected final String type;

    public GeoJSONPositionDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
