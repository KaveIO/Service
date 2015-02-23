package nl.kpmg.af.service.response.assembler;

import java.util.LinkedList;
import java.util.List;

import nl.kpmg.af.datamodel.model.Event;
import nl.kpmg.af.service.response.dto.EventDto;
import nl.kpmg.af.service.response.dto.GeoJSONPointDto;

/**
 * Utility class for disassembling Event objects into EventDto objects.
 *
 * @author Hoekstra.Maarten
 */
public final class EventAssembler {
    /**
     * Timestamp conversion factor.
     */
    private static final long FACTOR_SECONDS_MILLISECONDS = 1000L;

    /**
     * Private default constructor to make this class unconstructable.
     */
    private EventAssembler() {
    }

    /**
     * Disassembles Event object into EventDto object.
     *
     * @param event object to disassemble
     * @return disassembled event object
     */
    public static EventDto disassemble(final Event event) {
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
        if (event.getProcessingTimestamp() != null) {
            result.setProcessingTimestamp(
                    (int) (event.getProcessingTimestamp().getTime() / FACTOR_SECONDS_MILLISECONDS));
        }
        if (event.getMeasurementTimestamp() != null) {
            result.setMeasurementTimestamp(
                    (int) (event.getMeasurementTimestamp().getTime() / FACTOR_SECONDS_MILLISECONDS));
        }
        result.setValue(event.getValue());

        return result;
    }

    /**
     * Disassembles List of Event objects into List of EventDto objects.
     *
     * @param events objects to disassemble
     * @return disassembled node objects
     */
    public static List<EventDto> disassemble(final List<Event> events) {
        List<EventDto> result = new LinkedList();
        for (Event event : events) {
            result.add(EventAssembler.disassemble(event));
        }
        return result;
    }
}
