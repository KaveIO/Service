/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.response.dto;

/**
 * @author Hoekstra.Maarten
 */
public abstract class GeoJSONPositionDto {
    /**
     * The type of the GeoJSON position.
     */
    private final String type;

    /**
     * Sets the type of GeoJSONPosition this should be.
     *
     * @param type The type of the GeoJSON position
     */
    public GeoJSONPositionDto(final String type) {
        this.type = type;
    }

    /**
     * @return The type of the GeoJSON position.
     */
    public final String getType() {
        return type;
    }
}
