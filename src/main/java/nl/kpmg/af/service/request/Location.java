package nl.kpmg.af.service.request;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.LinkedList;
import java.util.Map;

import nl.kpmg.af.datamodel.dao.exception.DataModelException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    public DBObject getMongoCondition() throws DataModelException {
        DBObject condition = new BasicDBObject();

        if (near != null && within != null) {
            condition = new BasicDBObject("$or", new LinkedList() {
                {
                    add(near.getMongoCondition());
                    add(within.getMongoCondition());
                }
            });
        } else if (near != null) {
            condition = near.getMongoCondition();
        } else if (within != null) {
            condition = within.getMongoCondition();
        }
        return condition;
    }
}
