package nl.kpmg.af.service.request.filter.location;

public abstract class GeoJSONPosition {
    /**
     * The position in the list where the X-value (or longitude) is stored
     */
    protected static final int XVAL = 0;
    /**
     * The position in the list where the Y-value (or latitude) is stored
     */
    protected static final int YVAL = 1;
    /**
     * The type of the GeoJSON item
     */
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
