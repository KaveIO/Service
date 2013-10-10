package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;

import nl.kpmg.af.datamodel.model.Node;
import nl.kpmg.af.service.response.dto.GeoJSONPointDto;
import nl.kpmg.af.service.response.dto.NodeDto;

/**
 * Utility class for disassembling Node objects into NodeDto objects.
 * 
 * @author Hoekstra.Maarten
 */
public final class NodeAssembler {
    /**
     * Private default constructor to make this class unconstructable.
     */
    private NodeAssembler() {
    }

    /**
     * Disassembles Node object into NodeDto object.
     * 
     * @param node object to disassemble
     * @return disassembled node object
     */
    public static NodeDto disassemble(final Node node) {
        NodeDto result = new NodeDto();

        if (node.getId() != null) {
            result.setId(node.getId().toString());
        }
        result.setType(node.getType());
        result.setName(node.getName());
        result.setDescription(node.getDescription());
        result.setMeta(node.getMeta());
        if (node.getLatitude() != null && node.getLongitude() != null) {
            result.setLocation(new GeoJSONPointDto(node.getLongitude(), node.getLatitude()));
        }
        return result;
    }

    /**
     * Disassembles List of Node objects into List of NodeDto objects.
     * 
     * @param nodes objects to disassemble
     * @return disassembled node objects
     */
    public static List<NodeDto> disassemble(final List<Node> nodes) {
        List<NodeDto> result = new LinkedList();
        for (Node node : nodes) {
            result.add(NodeAssembler.disassemble(node));
        }
        return result;
    }
}
