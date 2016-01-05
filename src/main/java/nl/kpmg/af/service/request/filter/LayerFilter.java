/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter;

import nl.kpmg.af.service.request.filter.location.Location;
import nl.kpmg.af.service.request.filter.relation.Relation;
import nl.kpmg.af.service.request.filter.timestamp.Timestamp;

/**
 * @author Hoekstra.Maarten
 */
public final class LayerFilter {
    /**
     * Time based filter for the layer request.
     */
    private Timestamp timestamp;
    /**
     * Location based filter for the layer request.
     */
    private Location location;
    /**
     * Relation based filter for the layer request.
     */
    private Relation relation;

    /**
     * @return Time based filter for the layer request.
     */
    public Timestamp getTimeStamp() {
        return timestamp;
    }

    /**
     * @param timestamp Time based filter for the layer request.
     */
    public void setTimestamp(final Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Location based filter for the layer request.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location Location based filter for the layer request.
     */
    public void setLocation(final Location location) {
        this.location = location;
    }

    /**
     * @return Relation based filter for the layer request.
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @param relation Relation based filter for the layer request.
     */
    public void setRelation(final Relation relation) {
        this.relation = relation;
    }
}
