package nl.kpmg.af.service.request;

import nl.kpmg.af.service.request.filter.location.Location;
import com.mongodb.DBObject;
import nl.kpmg.af.datamodel.dao.MongoQuery;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import nl.kpmg.af.service.request.filter.NodeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRequest {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeRequest.class);
    private Integer limit = null;
    private NodeFilter filter = null;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public NodeFilter getFilter() {
        return filter;
    }

    public void setFilter(NodeFilter filter) {
        this.filter = filter;
    }

    public MongoQuery createMongoQuery() throws DataModelException {
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
}
