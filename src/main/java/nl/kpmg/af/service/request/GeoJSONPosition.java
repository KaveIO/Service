package nl.kpmg.af.service.request;

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

    /**
     * Abstract methods to convert the contents of the derived class to a JSON string
     *
     * @return a JSON string
     */
    public abstract String toJSONString();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
