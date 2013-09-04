package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;
import nl.kpmg.af.datamodel.model.Event;
import nl.kpmg.af.service.response.dto.EventDto;
import nl.kpmg.af.service.response.dto.GeoJSONPointDto;

/**
 *
 * @author Hoekstra.Maarten
 */
public class EventAssembler {
    public static EventDto disassemble(Event event) {
        EventDto result = new EventDto();

        if (event.getId() != null) {
            result.setId(event.getId().toString());
        }
        result.setLayer(event.getLayer());
        if (event.getNodeId() != null) {
            result.setNodeId(event.getNodeId().toString());
        }
        if (event.getEdgeId() != null) {
            result.setEdgeId(event.getEdgeId().toString());
        }
        if (event.getLatitude() != null && event.getLongitude() != null) {
            result.setLocation(new GeoJSONPointDto(event.getLongitude(), event.getLatitude()));
        }
        result.setPriority(event.getPriority());
        if (event.getTimestamp() != null) {
            result.setTimestamp((int) (event.getTimestamp().getTime() / 1000L));
        }
        result.setValue(event.getValue());

        return result;
    }

    public static List<EventDto> disassemble(List<Event> events) {
        List<EventDto> result = new LinkedList();
        for (Event event : events) {
            result.add(EventAssembler.disassemble(event));
        }
        return result;
    }
}
