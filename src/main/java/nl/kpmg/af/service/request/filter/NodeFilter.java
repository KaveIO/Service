package nl.kpmg.af.service.request.filter;

import nl.kpmg.af.service.request.filter.location.Location;

/**
 *
 * @author Hoekstra.Maarten
 */
public class NodeFilter {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
