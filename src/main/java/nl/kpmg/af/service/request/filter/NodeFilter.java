/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter;

import nl.kpmg.af.service.request.filter.location.Location;
import nl.kpmg.af.service.request.filter.relation.Relation;

/**
 * @author Hoekstra.Maarten
 */
public final class NodeFilter {
    /**
     * Location based filter for the layer request.
     */
    private Location location;

    /**
     * Relation based filter for the node request.
     */
    private Relation relation;

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
     * @return Relation based filter for the node request.
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @param relation Relation based filter for the node request.
     */
    public void setRelation(final Relation relation) {
        this.relation = relation;
    }
}
