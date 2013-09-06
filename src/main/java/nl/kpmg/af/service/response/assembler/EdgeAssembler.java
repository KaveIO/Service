package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;
import nl.kpmg.af.datamodel.model.Edge;
import nl.kpmg.af.service.response.dto.EdgeDto;

/**
 *
 * @author Hoekstra.Maarten
 */
public class EdgeAssembler {
    public static EdgeDto disassemble(Edge edge) {
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

    public static List<EdgeDto> disassemble(List<Edge> edges) {
        List<EdgeDto> result = new LinkedList();
        for (Edge edge : edges) {
            result.add(EdgeAssembler.disassemble(edge));
        }
        return result;
    }
}
