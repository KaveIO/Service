package nl.kpmg.af.service.response.dto;

import java.util.Map;

/**
 *
 * @author Hoekstra.Maarten
 */
public class NodeDto {
    private String id;
    private String type;
    private String name;
    private String description;
    private Map meta;
    private GeoJSONPointDto location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map getMeta() {
        return meta;
    }

    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public GeoJSONPointDto getLocation() {
        return location;
    }

    public void setLocation(GeoJSONPointDto location) {
        this.location = location;
    }
}
