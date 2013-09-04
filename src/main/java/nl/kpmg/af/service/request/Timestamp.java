package nl.kpmg.af.service.request;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.Date;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the information in the filter which governs the timestamp.
 * There are mutual exclusive options:
 * - the option "pastwindow" which represents the history of the data to retrieve in seconds
 * e.g. : "pastwindow" : 6000 will give all data beginngin with: (current time - 6000) up to now.
 * - the option "after" and "before" define a window in time
 *
 * @author Hoekstra.Maarten
 */
public class Timestamp {
    // Make getters and setters
    private Integer pastwindow = null;
    private Integer before = null;
    private Integer after = null;
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Timestamp.class);
    private static String MONGO_TIMESTAMP_FIELD = "timestamp";

    public int getPastwindow() {
        return pastwindow;
    }

    public void setPastwindow(int pastwindow) {
        this.pastwindow = pastwindow;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    public DBObject getMongoCondition() throws DataModelException {

        if (pastwindow != null && (after != null || before != null)) {
            LOGGER.error("Invalid list op options");
            throw new DataModelException("Can't use pastwindow and after/before in conjunction");
        }

        Date greaterThan = null;
        Date lesserThan = null;

        if (pastwindow != null && after == null && before == null) {
            long now = new Date().getTime();
            greaterThan = new Date(now - (pastwindow * 1000L));
        }
        if (pastwindow == null && after != null) {
            greaterThan = new Date(after * 1000L);
        }
        if (pastwindow == null && before != null) {
            lesserThan = new Date(before * 1000L);
        }

        BasicDBObject query = new BasicDBObject();
        BasicDBObject condition;
        if (greaterThan != null || lesserThan != null) {
            condition = new BasicDBObject();
            query.put(MONGO_TIMESTAMP_FIELD, condition);
            if (greaterThan != null) {
                condition.append("$gt", greaterThan);
            }
            if (lesserThan != null) {
                condition.append("$lt", lesserThan);
            }
        }

        return query;
    }
}
