package nl.kpmg.af.service.request.aggregation;

/**
 *
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
        return by;
    }

    /**
     * @param type Which type of ids are being filtered.
     */
    public void setBy(final String by) {
        this.by = by;
    }

    public AggregationType getType() {
        return type;
    }

    public void setType(AggregationType type) {
        this.type = type;
    }
}
