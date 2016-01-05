/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.response.assembler;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nl.kpmg.af.service.response.dto.FileDto;

import com.mongodb.DBObject;

/**
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
