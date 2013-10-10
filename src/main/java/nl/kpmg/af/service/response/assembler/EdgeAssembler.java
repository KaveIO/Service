package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;

import nl.kpmg.af.datamodel.model.Edge;
import nl.kpmg.af.service.response.dto.EdgeDto;

/**
 * Utility class for disassembling Edge objects into EdgeDto objects.
 * 
 * @author Hoekstra.Maarten
 */
public final class EdgeAssembler {
    /**
     * Private default constructor to make this class unconstructable.
     */
    private EdgeAssembler() {
    }

    /**
     * Disassembles Edge object into EdgeDto object.
     * 
     * @param edge object to disassemble
     * @return disassembled event object
     */
    public static EdgeDto disassemble(final Edge edge) {
        EdgeDto result = new EdgeDto();

        if (edge.getId() != null) {
            result.setId(edge.getId().toString());
        }
        result.setType(edge.getType());
        result.setName(edge.getName());
        result.setDescription(edge.getDescription());
        result.setMeta(edge.getMeta());
        if (edge.getSourceId() != null) {
            result.setSourceId(edge.getSourceId().toString());
        }
        if (edge.getTargetId() != null) {
            result.setTargetId(edge.getTargetId().toString());
        }
        result.setMeta(edge.getMeta());

        return result;
    }

    /**
     * Disassembles List of Edge objects into List of EdgeDto objects.
     * 
     * @param edges objects to disassemble
     * @return disassembled node objects
     */
    public static List<EdgeDto> disassemble(final List<Edge> edges) {
        List<EdgeDto> result = new LinkedList();
        for (Edge edge : edges) {
            result.add(EdgeAssembler.disassemble(edge));
        }
        return result;
    }
}
