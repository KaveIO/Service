package nl.kpmg.af.service.request.filter.relation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.List;
import nl.kpmg.af.datamodel.dao.exception.DataModelException;
import org.bson.types.ObjectId;

/**
 *
 * @author Hoekstra.Maarten
 */
public class Relation {
    private Type type;
    private List<ObjectId> ids;

    public enum Type {
        NODE("nodeId"),
        EDGE("edgeId");
        private final String fieldName;

        private Type(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<ObjectId> getIds() {
        return ids;
    }

    public void setIds(List<ObjectId> ids) {
        this.ids = ids;
    }

    public DBObject getMongoCondition() throws DataModelException {
        if (type != null && ids.size() > 0) {
            DBObject query = new BasicDBObject(type.getFieldName(), new BasicDBObject("$in", ids));
            return query;
        } else {
            throw new DataModelException("Malformed request");
        }
    }
}
