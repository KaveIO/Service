package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nl.kpmg.af.datamodel.model.Node;
import nl.kpmg.af.service.response.dto.GeoJSONPointDto;
import nl.kpmg.af.service.response.dto.NodeDto;

/**
 *
 * @author Hoekstra.Maarten
 */
public class NodeAssembler {
    private String type;
    private String name;
    private String description;
    private Map meta;
    private GeoJSONPointDto location;

    public static NodeDto disassemble(Node node) {
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

    public static List<NodeDto> disassemble(List<Node> nodes) {
        List<NodeDto> result = new LinkedList();
        for (Node node : nodes) {
            result.add(NodeAssembler.disassemble(node));
        }
        return result;
    }
}
