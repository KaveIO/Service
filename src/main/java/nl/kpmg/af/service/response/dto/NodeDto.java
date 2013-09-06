package nl.kpmg.af.service.response.dto;

import java.util.Map;

/**
 * Data Transfer Object for Node.
 * The auto-serialization of JAX-RS is really convenient but will serialize complex objects (such as ObjectId) in an
 * undesirable fashion. In order to avoid this problem a couple of DTO's are made. These are used to transfer business
 * objects to client applications.
 *
 * @author Hoekstra.Maarten
 */
public final class NodeDto {
    /**
     * The unique mongo id of this object.
     */
    private String id;
    /**
     * The type to which this object belongs.
     */
    private String type;
    /**
     * The name of this node.
     */
    private String name;
    /**
     * The description of this node.
     */
    private String description;
    /**
     * A map with the specific contents of this node.
     */
    private Map meta;
    /**
     * The location expressed in GeoJSON of this node.
     */
    private GeoJSONPointDto location;

    /**
     * @return The unique mongo id of this object.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The unique mongo id of this object.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return The type to which this object belongs.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to which this object belongs.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return The name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of this node.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return The description of this node.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description of this node.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return A map with the specific contents of this node.
     */
    public Map getMeta() {
        return meta;
    }

    /**
     * @param meta A map with the specific contents of this node.
     */
    public void setMeta(final Map meta) {
        this.meta = meta;
    }

    /**
     * @return The location expressed in GeoJSON of this node.
     */
    public GeoJSONPointDto getLocation() {
        return location;
    }

    /**
     * @param location The location expressed in GeoJSON of this node.
     */
    public void setLocation(final GeoJSONPointDto location) {
        this.location = location;
    }
}
