/**
 * 
 */
package nl.kpmg.af.dao;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author janos4276
 *
 */
public class LayerDao extends AbstractDao {

	/**
	 * @param connectionString
	 * @param dbName
	 */
	public LayerDao() {
		super("", "");
	}

	public List<DBObject> get(String n) {
		DBCollection collection = getMongoDB().getCollection(n+"Layer");
		BasicDBObject keys = new BasicDBObject();
		keys.put("history", 1);
		DBCursor cursor = collection.find(new BasicDBObject(), keys);
		return cursor.toArray();
	}
}
