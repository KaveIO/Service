package nl.kpmg.af.service.response.assembler;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import nl.kpmg.af.datamodel.model.Input;
import nl.kpmg.af.datamodel.model.input.ConcreteInput;
import nl.kpmg.af.service.request.InputRequest;
import nl.kpmg.af.service.response.dto.InputDto;

/**
 * Utility class for assembling InputDto objects into Input objects.
 * 
 * @author Hoekstra.Maarten, Jori van Lier
 */
public final class InputAssembler {
    /**
     * Timestamp conversion factor.
     */
    private static final long FACTOR_SECONDS_MILLISECONDS = 1000L;


    /**
     * Private default constructor to make this class unconstructable.
     */
    private InputAssembler() {
    }

    /**
     * Assembles EventDto object into Input object.
     * 
     * @param type The input type.
     * @param inputDto The input data transfer object.
     * @return Input
     */
    public static Input assemble(final String type, final InputRequest inputDto) {
        Input input = new ConcreteInput();
        input.setType(type);
        input.setTimestamp(new Date());
        if (inputDto.getValue() != null) {
            input.setValue(inputDto.getValue());
        }
        return input;
    }

    /**
     * Disassembles Node object into NodeDto object.
     * 
     * @param input object to disassemble
     * @return disassembled node object
     */
    public static InputDto disassemble(final Input input) {
        InputDto result = new InputDto();

        if (input.getId() != null) {
            result.setId(input.getId().toString());
        }
        result.setValue(input.getValue());

        if (input.getType() != null) {
            result.setType(input.getType());
        }

        if (input.getTimestamp() != null) {
            result.setTimestamp(
                (int) (input.getTimestamp().getTime() / FACTOR_SECONDS_MILLISECONDS));
        }

        return result;
    }

    /**
     * Disassembles List of Input objects into List of InputDto objects.
     * 
     * @param inputs objects to disassemble.
     * @return disassembled node objects
     */
    public static List<InputDto> disassemble(final List<Input> inputs) {
        List<InputDto> result = new LinkedList();
        for (Input input : inputs) {
            result.add(InputAssembler.disassemble(input));
        }
        return result;
    }
}
