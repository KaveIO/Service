/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter.location;

/**
 * Abstract class for some shared functionality between GeoJSON data types.
 *
 * @author Hoekstra.Maarten
 */
public abstract class GeoJSONPosition {
    /**
     * The position in the list where the X-value (or longitude) is stored.
     */
    protected static final int XVAL = 0;
    /**
     * The position in the list where the Y-value (or latitude) is stored.
     */
    protected static final int YVAL = 1;
    /**
     * The type of the GeoJSON item.
     */
    private String type;

    /**
     * @return The type of the GeoJSON item.
     */
    public final String getType() {
        return type;
    }

    /**
     * @param type The type of the GeoJSON item.
     */
    public final void setType(final String type) {
        this.type = type;
    }
}
