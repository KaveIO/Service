package nl.kpmg.af.service.request;

import nl.kpmg.af.service.request.filter.location.Location;
import com.mongodb.DBObject;
import nl.kpmg.af.datamodel.dao.MongoQuery;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.filter.NodeFilter;

/**
 * Top level request object for the node service.
 *
 * @author Hoekstra.Maarten
 */
public final class NodeRequest {
    /**
     * The maximum amount of objects to be returned.
     */
    private Integer limit = null;
    /**
     * The more complex filter parameters used to fetch only a subset of all objects.
     */
    private NodeFilter filter = null;

    /**
     * Transforms this NodeRequest object in its corresponding DBObject.
     *
     * @return NodeRequest as a mongo query
     * @throws InvalidRequestException thrown if the request parameters aren't correctly interpretable.
     */
    public MongoQuery createMongoQuery() throws InvalidRequestException {
        MongoQuery query = new MongoQuery();
        DBObject queryComponents = query.getDBObj();
        if (filter != null) {
            Location location = filter.getLocation();
            if (location != null) {
                DBObject locationfilter = location.getMongoCondition();
                queryComponents.putAll(locationfilter);
            }
        }
        return query;
    }

    /**
     * @return The maximum amount of objects to be returned.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit The maximum amount of objects to be returned.
     */
    public void setLimit(final Integer limit) {
        this.limit = limit;
    }

    /**
     * @return The more complex filter parameters used to fetch only a subset of all objects.
     */
    public NodeFilter getFilter() {
        return filter;
    }

    /**
     * @param filter The more complex filter parameters used to fetch only a subset of all objects.
     */
    public void setFilter(final NodeFilter filter) {
        this.filter = filter;
    }
}
