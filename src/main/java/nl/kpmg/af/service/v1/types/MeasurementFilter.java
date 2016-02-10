/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

import com.mongodb.util.JSONParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author mhoekstra
 * @deprecated 
 */
public class MeasurementFilter implements Map {

    Map innerMap = new HashMap();

    /**
     * The maximum amount of objects to be returned.
     */
    public Integer getLimit() {
        if (innerMap.containsKey("limit")) {
            return (Integer) innerMap.get("limit");
        }
        return -1;
    }

    /**
     * The order based on timestamp in which the events are returned.
     */
    public Sort getSort() throws SortCastException {
        Sort sort = new Sort(Direction.DESC, "measurementTimestamp");

        if (innerMap.containsKey("sort")) {


            if (List.class.isAssignableFrom(innerMap.get("sort").getClass())) {

                List sortStatements = (List) innerMap.get("sort");
                Map<String, Order> orders = new HashMap();


                for (Object sortStatement : sortStatements) {

                    if (Map.class.isAssignableFrom(sortStatement.getClass())) {

                        Map<Object, Object> query = (Map) sortStatement;

                        if (query.size() != 1) {
                            throw new SortCastException("Multiple clauses in single sort statement.");
                        }

                        for (Map.Entry<Object, Object> entry : query.entrySet()) {
                            if (String.class.equals(entry.getKey().getClass())
                                    && Integer.class.equals(entry.getValue().getClass())) {

                                Direction direction = entry.getValue().equals(0) ? Direction.DESC : Direction.ASC;
                                String field = (String) entry.getKey();

                                Order order = new Order(direction, field);
                                if (orders.containsKey(field)) {
                                    throw new SortCastException("Dupplicate sort clause key");
                                } else {
                                    orders.put(field, order);
                                }
                            } else {
                               throw new SortCastException("Unparsable sort statement clause.");
                            }
                        }
                    } else {
                        throw new SortCastException("No map detected in sort statement clause.");
                    }
                }

                // Parsing is a success. We can sort this out.
                if (orders.size() > 0) {
                    sort =  new Sort(new LinkedList(orders.values()));
                }

            } else {
                throw new SortCastException("No sort statement list detected in sort field.");
            }
        }
        return sort;
    }

    /**
     * The more complex filter parameters used to fetch only a subset of all objects.
     */
    public Query getQuery() throws QueryCastException {
        if (innerMap.containsKey("query")) {

            if (String.class.isAssignableFrom(innerMap.get("query").getClass())) {
                String query = (String) innerMap.get("query");
                try {
                    Query basicQuery = new BasicQuery(query);
                    return basicQuery;
                } catch(JSONParseException ex) {
                    throw new QueryCastException(query, ex);
                }
            } else {
                throw new QueryCastException();
            }
        }
        return new Query();
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return innerMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return innerMap.get(key);
    }

    @Override
    public Object put(Object arg0, Object arg1) {
        return innerMap.put(arg0, arg1);
    }

    @Override
    public Object remove(Object key) {
        return innerMap.remove(key);
    }

    @Override
    public void putAll(Map m) {
        innerMap.putAll(m);
    }

    @Override
    public void clear() {
        innerMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return innerMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return innerMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return innerMap.equals(o);
    }

    @Override
    public int hashCode() {
        return innerMap.hashCode();
    }
}
