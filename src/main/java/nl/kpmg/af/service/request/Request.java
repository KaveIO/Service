package nl.kpmg.af.service.request;

import com.mongodb.DBObject;
import nl.kpmg.af.datamodel.dao.MongoOrder;
import nl.kpmg.af.datamodel.dao.MongoQuery;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);
    private Integer sort = null;
    private Integer limit = null;
    private Filter filter = null;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public MongoQuery createMongoQuery() throws DataModelException {
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

    public MongoOrder createMongoOrder() {
        MongoOrder order = (sort != null) ? new MongoOrder(sort) : new MongoOrder();
        return order;
    }
}
