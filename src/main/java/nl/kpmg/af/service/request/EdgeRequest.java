package nl.kpmg.af.service.request;

import nl.kpmg.af.service.request.filter.location.Location;
import com.mongodb.DBObject;
import nl.kpmg.af.datamodel.dao.MongoQuery;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.request.filter.EdgeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgeRequest {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EdgeRequest.class);
    private Integer limit = null;
    private EdgeFilter filter = null;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public EdgeFilter getFilter() {
        return filter;
    }

    public void setFilter(EdgeFilter filter) {
        this.filter = filter;
    }

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
}
