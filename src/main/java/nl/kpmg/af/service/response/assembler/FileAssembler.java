package nl.kpmg.af.service.response.assembler;

import com.mongodb.DBObject;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import nl.kpmg.af.service.response.dto.FileDto;

/**
 *
 * @author Hoekstra.Maarten
 */
public class FileAssembler {
    public static FileDto disassemble(final DBObject file) {
        FileDto result = new FileDto();
        result.setFilename((String) file.get("filename"));
        result.setSize((Long) file.get("length"));
        result.setMd5((String) file.get("md5"));
        result.setCreate((Date) file.get("uploadDate"));
        return result;
    }

    public static List<FileDto> disassemble(final Iterable<DBObject> files) {
        List<FileDto> result = new LinkedList();
        for (DBObject file : files) {
            result.add(FileAssembler.disassemble(file));
        }
        return result;
    }
}
