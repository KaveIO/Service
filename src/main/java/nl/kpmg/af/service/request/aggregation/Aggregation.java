/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.request.aggregation;

/**
 * @author Hoekstra.Maarten
 */
public final class Aggregation {
    /**
     * Which type of ids are being filtered.
     */
    private String by;
    private AggregationType type;

    /**
     * @return Which type of ids are being filtered.
     */
    public String getBy() {
        return this.by;
    }

    /**
     * @param by Which type of ids are being filtered.
     */
    public void setBy(final String by) {
        this.by = by;
    }

    public AggregationType getType() {
        return this.type;
    }

    public void setType(final AggregationType type) {
        this.type = type;
    }
}
