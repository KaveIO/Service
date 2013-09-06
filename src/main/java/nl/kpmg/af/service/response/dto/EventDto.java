package nl.kpmg.af.service.response.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for Event.
 * The auto-serialization of JAX-RS is really convenient but will serialize complex objects (such as ObjectId) in an
 * undesirable fashion. In order to avoid this problem a couple of DTO's are made. These are used to transfer business
 * objects to client applications.
 *
 * @author Hoekstra.Maarten
 */
public final class EventDto {
    /**
     * The unique mongo id of this object.
     */
    private String id;
    /**
     * The layer from which this object was fetched.
     */
    private String layer;
    /**
     * The mongo id of the related node.
     */
    private String nodeId;
    /**
     * The mongo id of the related edge.
     */
    private String edgeId;
    /**
     * The location expressed in GeoJSON where this event took place.
     */
    private GeoJSONPositionDto location;
    /**
     * The urgency of this event.
     */
    private String priority;
    /**
     * The timestamp expressed in seconds from epoch on which this event took place.
     */
    private Integer timestamp;
    /**
     * A map with the specific contents of this event.
     */
    private Map value = new HashMap();

    /**
     * @return The unique mongo id of this object
     */
    public final String getId() {
        return id;
    }

    /**
     * @param id The unique mongo id of this object
     */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * @return The layer from which this object was fetched.
     */
    public final String getLayer() {
        return layer;
    }

    /**
     * @param layer The layer from which this object was fetched.
     */
    public final void setLayer(final String layer) {
        this.layer = layer;
    }

    /**
     * @return The mongo id of the related node.
     */
    public final String getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId The mongo id of the related node.
     */
    public final void setNodeId(final String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @return The mongo id of the related edge.
     */
    public final String getEdgeId() {
        return edgeId;
    }

    /**
     * @param edgeId The mongo id of the related edge.
     */
    public final void setEdgeId(final String edgeId) {
        this.edgeId = edgeId;
    }

    /**
     * @return The location expressed in GeoJSON where this event took place.
     */
    public final GeoJSONPositionDto getLocation() {
        return location;
    }

    /**
     * @param location The location expressed in GeoJSON where this event took place.
     */
    public final void setLocation(final GeoJSONPositionDto location) {
        this.location = location;
    }

    /**
     * @return The urgency of this event.
     */
    public final String getPriority() {
        return priority;
    }

    /**
     * @param priority The urgency of this event.
     */
    public final void setPriority(final String priority) {
        this.priority = priority;
    }

    /**
     * @return The timestamp expressed in seconds from epoch on which this event took place.
     */
    public final Integer getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp expressed in seconds from epoch on which this event took place.
     */
    public final void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return A map with the specific contents of this event.
     */
    public final Map getValue() {
        return value;
    }

    /**
     * @param value A map with the specific contents of this event.
     */
    public final void setValue(final Map value) {
        this.value = value;
    }
}
