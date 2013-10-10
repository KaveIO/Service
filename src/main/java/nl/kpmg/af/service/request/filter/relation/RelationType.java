package nl.kpmg.af.service.request.filter.relation;

/**
 * @author Hoekstra.Maarten
 */
public enum RelationType {
    /**
     * relation type associated nodes.
     */
    NODE("nodeId"),
    /**
     * relation type associated edges.
     */
    EDGE("edgeId"),
    /**
     * relation type associated MongoIds.
     */
    ID("_id");
    /**
     * actual used field for relation.
     */
    private final String fieldName;

    /**
     * @param fieldName actual used field for relation.
     */
    private RelationType(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return actual used field for relation.
     */
    public final String getFieldName() {
        return fieldName;
    }
}
