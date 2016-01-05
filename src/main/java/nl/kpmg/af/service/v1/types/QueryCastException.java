/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

/**
 *
 * @author mhoekstra
 */
public class QueryCastException extends Exception {

    String query = "";

    public QueryCastException() {
        super();
    }

    public QueryCastException(String query) {
        this.query = query;
    }

    public QueryCastException(String query, Exception ex) {
        super(ex);
        this.query = query;
    }
}
