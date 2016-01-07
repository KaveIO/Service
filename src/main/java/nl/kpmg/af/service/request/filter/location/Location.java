/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.filter.location;

import nl.kpmg.af.service.exception.InvalidRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Location part of a filter request.
 *
 * @author Hoekstra.Maarten
 */
public final class Location {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Location.class);
    /**
     * Filter specification for fetching only objects near a specific point.
     */
    private Near near;
    /**
     * Filter specification for fetching only objects within a polygon.
     */
    private Within within;

    /**
     * Transforms this Location object in its corresponding DBObject.
     *
     * @return Location as a mongo query
     * @throws InvalidRequestException thrown if the request parameters aren't correctly interpretable.
     */
    public DBObject getMongoCondition() throws InvalidRequestException {
        DBObject condition = new BasicDBObject();
        if (near != null && within != null) {
            // http://docs.mongodb.org/manual/reference/limits/
            LOGGER.error("Invalid list op options");
            throw new InvalidRequestException("Can't use near and within in conjunction");
        } else if (near != null) {
            condition = new BasicDBObject("location", near.getMongoCondition());
        } else if (within != null) {
            condition = new BasicDBObject("location", within.getMongoCondition());
        }
        return condition;
    }

    /**
     * @return Filter specification for fetching only objects near a specific point.
     */
    public Near getNear() {
        return near;
    }

    /**
     * @param near Filter specification for fetching only objects near a specific point.
     */
    public void setNear(final Near near) {
        this.near = near;
    }

    /**
     * @return Filter specification for fetching only objects within a polygon.
     */
    public Within getWithin() {
        return within;
    }

    /**
     * @param within Filter specification for fetching only objects within a polygon.
     */
    public void setWithin(final Within within) {
        this.within = within;
    }
}
