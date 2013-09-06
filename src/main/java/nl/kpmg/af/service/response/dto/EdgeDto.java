package nl.kpmg.af.service.response.dto;

import java.util.Map;

/**
 * Data Transfer Object for Edge.
 * The auto-serialization of JAX-RS is really convenient but will serialize complex objects (such as ObjectId) in an
 * undesirable fashion. In order to avoid this problem a couple of DTO's are made. These are used to transfer business
 * objects to client applications.
 *
 * @author Hoekstra.Maarten
 */
public class EdgeDto {
    /**
     * The unique mongo id of this object.
     */
    private String id;
    /**
     * The type to which this object belongs.
     */
    private String type;
    /**
     * The name of this edge.
     */
    private String name;
    /**
     * The description of this edge.
     */
    private String description;
    /**
     * A map with the specific contents of this edge.
     */
    private Map meta;
    /**
     * The mongo id of the source node to this edge.
     */
    private String sourceId;
    /**
     * The mongo id of the target node to this edge.
     */
    private String targetId;

    /**
     * @return The unique mongo id of this object.
     */
    public final String getId() {
        return id;
    }

    /**
     * @param id The unique mongo id of this object.
     */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * @return type The type to which this object belongs.
     */
    public final String getType() {
        return type;
    }

    /**
     * @param type The type to which this object belongs.
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /**
     * @return The name of this edge.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name The name of this edge.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return A map with the specific contents of this edge.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param description A map with the specific contents of this edge.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return A map with the specific contents of this edge.
     */
    public final Map getMeta() {
        return meta;
    }

    /**
     * @param meta A map with the specific contents of this edge.
     */
    public void setMeta(final Map meta) {
        this.meta = meta;
    }

    /**
     * @return The mongo id of the source node to this edge.
     */
    public final String getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId The mongo id of the source node to this edge.
     */
    public final void setSourceId(final String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return The mongo id of the target node to this edge.
     */
    public final String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId The mongo id of the target node to this edge.
     */
    public final void setTargetId(final String targetId) {
        this.targetId = targetId;
    }
}
