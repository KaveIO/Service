package nl.kpmg.af.service.request.filter.relation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.List;
import nl.kpmg.af.service.exception.InvalidRequestException;
import org.bson.types.ObjectId;

/**
 *
 * @author Hoekstra.Maarten
 */
public final class Relation {
    /**
     * Which type of ids are being filtered.
     */
    private RelationType type;
    /**
     * List of ids to which the relation should be constrained.
     */
    private List<ObjectId> ids;

    /**
     * Transforms this Relation object in its corresponding DBObject.
     *
     * @return Relation filter as a mongo query
     * @throws InvalidRequestException thrown if the request parameters aren't correctly interpretable.
     */
    public DBObject getMongoCondition() throws InvalidRequestException {
        if (type != null && ids.size() > 0) {
            DBObject query = new BasicDBObject(type.getFieldName(), new BasicDBObject("$in", ids));
            return query;
        } else {
            throw new InvalidRequestException("Malformed request");
        }
    }

    /**
     * @return Which type of ids are being filtered.
     */
    public RelationType getType() {
        return type;
    }

    /**
     * @param type Which type of ids are being filtered.
     */
    public void setType(final RelationType type) {
        this.type = type;
    }

    /**
     * @return List of ids to which the relation should be constrained.
     */
    public List<ObjectId> getIds() {
        return ids;
    }

    /**
     * @param ids List of ids to which the relation should be constrained.
     */
    public void setIds(final List<ObjectId> ids) {
        this.ids = ids;
    }
}
