package nl.kpmg.af.service.request;

import nl.kpmg.af.service.request.filter.LayerFilter;
import nl.kpmg.af.service.request.filter.location.Location;
import nl.kpmg.af.service.request.filter.relation.Relation;
import nl.kpmg.af.service.request.filter.timestamp.Timestamp;
import com.mongodb.DBObject;
import nl.kpmg.af.datamodel.dao.MongoOrder;
import nl.kpmg.af.datamodel.dao.MongoQuery;
import nl.kpmg.af.service.exception.InvalidRequestException;

/**
 * Top level request object for the layer service.
 *
 * @author Hoekstra.Maarten
 */
public class LayerRequest {
    /**
     * The order based on timestamp in which the events are returned.
     */
    private Integer sort = null;
    /**
     * The maximum amount of objects to be returned.
     */
    private Integer limit = null;
    /**
     * The more complex filter parameters used to fetch only a subset of all objects.
     */
    private LayerFilter filter = null;

    /**
     * Transforms this layerRequest object in its corresponding DBObject.
     *
     * @return layerRequest as a mongo query
     * @throws InvalidRequestException thrown if the request parameters aren't correctly interpretable.
     */
    public final MongoQuery createMongoQuery() throws InvalidRequestException {
        MongoQuery query = new MongoQuery();
        DBObject queryComponents = query.getDBObj();
        if (filter != null) {
            Timestamp timestamp = filter.getTimeStamp();
            if (timestamp != null) {
                DBObject timefilter = timestamp.getMongoCondition();
                queryComponents.putAll(timefilter);
            }
            Location location = filter.getLocation();
            if (location != null) {
                DBObject locationfilter = location.getMongoCondition();
                queryComponents.putAll(locationfilter);
            }
            Relation relation = filter.getRelation();
            if (relation != null) {
                DBObject relationfilter = relation.getMongoCondition();
                queryComponents.putAll(relationfilter);
            }
        }
        return query;
    }

    /**
     * Transforms sort in its corresponding MongoOrder.
     *
     * @return sort as a MongoOrder
     */
    public final MongoOrder createMongoOrder() {
        if (sort != null) {
            return new MongoOrder(sort);
        } else {
            return new MongoOrder();
        }
    }

    /**
     * @return The order based on timestamp in which the events are returned.
     */
    public final int getSort() {
        return sort;
    }

    /**
     * @param sort The order based on timestamp in which the events are returned.
     */
    public final void setSort(final int sort) {
        this.sort = sort;
    }

    /**
     * @return The maximum amount of objects to be returned.
     */
    public final int getLimit() {
        return limit;
    }

    /**
     * @param limit The maximum amount of objects to be returned.
     */
    public final void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * @return The more complex filter parameters used to fetch only a subset of all objects.
     */
    public final LayerFilter getFilter() {
        return filter;
    }

    /**
     * @param filter The more complex filter parameters used to fetch only a subset of all objects.
     */
    public final void setFilter(final LayerFilter filter) {
        this.filter = filter;
    }
}
