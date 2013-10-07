package nl.kpmg.af.service.response.assembler;

import java.util.Date;
import nl.kpmg.af.datamodel.model.Input;
import nl.kpmg.af.datamodel.model.input.ConcreteInput;
import nl.kpmg.af.service.request.InputRequest;

/**
 * Utility class for assembling InputDto objects into Input objects.
 *
 * @author Hoekstra.Maarten
 */
public final class InputAssembler {
    /**
     * Private default constructor to make this class unconstructable.
     */
    private InputAssembler() {
    }

    /**
     * Disassembles Event object into EventDto object.
     *
     * @param event object to disassemble
     * @return disassembled event object
     */
    public static Input assemble(String type, final InputRequest inputDto) {
        Input input = new ConcreteInput();
        input.setType(type);
        input.setTimestamp(new Date());
        if (inputDto.getValue() != null) {
            input.setValue(inputDto.getValue());
        }
        return input;
    }
}
