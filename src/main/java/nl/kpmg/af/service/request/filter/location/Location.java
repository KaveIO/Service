package nl.kpmg.af.service.request.filter.location;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import nl.kpmg.af.service.exception.InvalidRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hoekstra.Maarten
 */
public class Location {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Location.class);
    private Near near;
    private Within within;

    public Location(Near near, Within within) {
        this.near = near;
        this.within = within;
    }

    public Location(Near near) {
        this(near, null);
    }

    public Location(Within within) {
        this(null, within);
    }

    public Location() {
        this(null, null);
    }

    public Near getNear() {
        return near;
    }

    public void setNear(Near near) {
        this.near = near;
    }

    public Within getWithin() {
        return within;
    }

    public void setWithin(Within within) {
        this.within = within;
    }

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
}
