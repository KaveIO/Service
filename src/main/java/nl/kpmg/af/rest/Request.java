package nl.kpmg.af.rest;

import nl.kpmg.af.datamodel.dao.filter.Filter;
import nl.kpmg.af.datamodel.dao.filter.MongoOrder;

class Request {
    public Integer sort = null;
    public Integer limit = null;
    public Filter filter = null;

    
    public MongoOrder getMongoOrder(){
        MongoOrder or = (sort != null) ? new MongoOrder(sort) : new MongoOrder();
        return or;
    }

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
}
