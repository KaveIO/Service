package nl.kpmg.af.service.request;

import java.util.List;

/**
 *
 * @author Hoekstra.Maarten
 */
public class GeoJSONPolygon extends GeoJSONPosition {
    /**
     * Because JBOSS wants this...
     */
    private List<List<Double>> coordinates;

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toJSONString() {

        String theListStr = "";
        for (List<Double> coord : coordinates) {
            if (!theListStr.isEmpty()) {
                theListStr = String.format("%s, [%f, %f]", theListStr,
                        coord.get(XVAL), coord.get(YVAL));
            } else {
                theListStr = String.format("[%f, %f]",
                        coord.get(XVAL), coord.get(YVAL));
            }
        }
        String coordListString = String.format("[%s]", theListStr);
        String val = String.format(
                "{\"type\" : \"%s\" , \"coordinates\" : [%s] }", type,
                coordListString);
        return val;
    }
}
