package nl.kpmg.af.service.request.filter.timestamp;

import java.util.Date;

import nl.kpmg.af.service.exception.InvalidRequestException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class contains the information in the filter which governs the timestamp.
 * There are mutual exclusive options:
 * - the option "pastwindow" which represents the history of the data to retrieve in seconds
 * e.g. : "pastwindow" : 6000 will give all data beginngin with: (current time - 6000) up to now.
 * - the option "after" and "before" define a window in time
 * 
 * @author Hoekstra.Maarten
 */
public final class Timestamp {
    /**
     * Field name of the timestamp in mongo.
     */
    private static final String MONGO_TIMESTAMP_FIELD = "measurementTimestamp";
    /**
     * Constant multiplication factor to move from seconds to milliseconds.
     */
    private static final long FACTOR_SECONDS_MILLISECONDS = 1000L;
    /**
     * Filter field which determines the amount of seconds to look in the past.
     * This can be used to fetch object not older than [pastwindow] seconds ago
     */
    private Integer pastwindow = null;
    /**
     * Filter field which determines the timestamp of which returned objects need to be younger then.
     * Timestamp as seconds from epoch
     */
    private Integer before = null;
    /**
     * Filter field which determines the timestamp of which returned objects need to be older then.
     * Timestamp as seconds from epoch
     */
    private Integer after = null;

    /**
     * Transforms this timestamp object in its corresponding DBObject.
     * 
     * @return Timestamp filter as a mongo query
     * @throws InvalidRequestException thrown if the request parameters aren't correctly interpretable.
     */
    public DBObject getMongoCondition() throws InvalidRequestException {
        if (pastwindow != null && (after != null || before != null)) {
            throw new InvalidRequestException("Can't use pastwindow and after/before in conjunction");
        }

        Date greaterThan = null;
        Date lesserThan = null;

        if (pastwindow != null && after == null && before == null) {
            long now = new Date().getTime();
            greaterThan = new Date(now - (pastwindow * FACTOR_SECONDS_MILLISECONDS));
        }
        if (pastwindow == null && after != null) {
            greaterThan = new Date(after * FACTOR_SECONDS_MILLISECONDS);
        }
        if (pastwindow == null && before != null) {
            lesserThan = new Date(before * FACTOR_SECONDS_MILLISECONDS);
        }

        return this.getMongoCondition(greaterThan, lesserThan);
    }

    /**
     * Helper function for transforming the calculated greaterThan and lesserThan values into a mongo query.
     * 
     * @param greaterThan Date value
     * @param lesserThan Date value
     * @return greaterThan & lesserThan as a mongo query
     */
    private DBObject getMongoCondition(final Date greaterThan, final Date lesserThan) {
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

    /**
     * @return Filter field which determines the amount of seconds to look in the past.
     */
    public int getPastwindow() {
        return pastwindow;
    }

    /**
     * @param pastwindow Filter field which determines the amount of seconds to look in the past.
     */
    public void setPastwindow(final int pastwindow) {
        this.pastwindow = pastwindow;
    }

    /**
     * @return Filter field which determines the timestamp of which returned objects need to be younger then.
     */
    public int getBefore() {
        return before;
    }

    /**
     * @param before Filter field which determines the timestamp of which returned objects need to be younger then.
     */
    public void setBefore(final int before) {
        this.before = before;
    }

    /**
     * @return Filter field which determines the timestamp of which returned objects need to be older then.
     */
    public int getAfter() {
        return after;
    }

    /**
     * @param after Filter field which determines the timestamp of which returned objects need to be older then.
     */
    public void setAfter(final int after) {
        this.after = after;
    }
}
