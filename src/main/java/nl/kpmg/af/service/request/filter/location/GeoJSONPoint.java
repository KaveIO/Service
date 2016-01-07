/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter.location;

import java.util.List;

/**
 * @author Hoekstra.Maarten
 */
public final class GeoJSONPoint extends GeoJSONPosition {
    /**
     * A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    private List<Double> coordinates;

    /**
     * @return A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates A list with only two values containing the longitude and latitude of this GeoJSON object.
     */
    public void setCoordinates(final List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return helper function for directly fetching the longitude from the coordinates list.
     */
    public double getLongitude() {
        return coordinates.get(XVAL);
    }

    /**
     * @return helper function for directly fetching the latitude from the coordinates list.
     */
    public double getLatitude() {
        return coordinates.get(YVAL);
    }
}
