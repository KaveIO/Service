package nl.kpmg.af.service.request;

/**
 *
 * @author Hoekstra.Maarten
 */
public class Filter {
    private Timestamp timestamp;
    private Location location;
    private Relation relation;

    public Timestamp getTimeStamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}
