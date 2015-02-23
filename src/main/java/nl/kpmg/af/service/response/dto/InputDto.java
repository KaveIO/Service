package nl.kpmg.af.service.response.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for Input
 * 
 * @author Jori van Lier
 */
public final class InputDto {
    /**
     * The unique mongo id of this object.
     */
    private String id;
    /**
     * The type to which this object belongs.
     */
    private String type;
    /**
     * The timestamp expressed in seconds from epoch on which this event took place.
     */
    private Integer timestamp;

    /**
     * A map with the specific contents of this event.
     */
    private Map value = new HashMap();

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
