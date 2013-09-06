package nl.kpmg.af.service.response.dto;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hoekstra.Maarten
 */
public class EventDto {
    private String id;
    private String layer;
    private String nodeId;
    private String edgeId;
    private GeoJSONPositionDto location;
    private String priority;
    private Integer timestamp;
    private Map value = new HashMap();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public GeoJSONPositionDto getLocation() {
        return location;
    }

    public void setLocation(GeoJSONPositionDto location) {
        this.location = location;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Map getValue() {
        return value;
    }

    public void setValue(Map value) {
        this.value = value;
    }
}
