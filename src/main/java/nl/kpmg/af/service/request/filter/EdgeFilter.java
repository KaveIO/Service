package nl.kpmg.af.service.request.filter;

import nl.kpmg.af.service.request.filter.location.Location;

/**
 * @author Hoekstra.Maarten
 */
public final class EdgeFilter {
    /**
     * Location based filter for the layer request.
     */
    private Location location;

    /**
     * @return Location based filter for the layer request.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location Location based filter for the layer request.
     */
    public void setLocation(final Location location) {
        this.location = location;
    }
}
