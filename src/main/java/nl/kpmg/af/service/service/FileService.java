package nl.kpmg.af.service.service;

import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.kpmg.af.datamodel.connection.MongoDatabase;
import nl.kpmg.af.datamodel.connection.exception.MongoAuthenticationException;
import nl.kpmg.af.service.MongoDBUtil;
import nl.kpmg.af.service.response.assembler.FileAssembler;
import nl.kpmg.af.service.response.dto.FileDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

/**
 * This class represents the edges rest service.
 * Right now it's a Java re-write of the current middleware layer service.
 * This service can be reached via http://jbosshost/Services/rest/layer, where
 * the relative path "rest" is defined in Activator.java.
 *
 * @author janos4276
 */
@Path("{applicationId}/files")
public final class FileService {
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    /**
     * Get the corresponding json for the "collection" collection.
     *
     * @param collection the collection of edges to fetch from
     * @return the list of edges
     */
    @GET
    @Path("{collection}")
    @Produces("application/json")
    public Response get(@PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection) {
        try {
            MongoDatabase applicationDatabase = MongoDBUtil.getApplicationDatabase(applicationId);
            GridFS gridFS = new GridFS(applicationDatabase.getDatabase(), collection);
            DBCursor fileList = gridFS.getFileList();
            List<FileDto> result = FileAssembler.disassemble(fileList);
            return Response.ok(result).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (UnknownHostException | MongoAuthenticationException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get the corresponding json for the "collection" collection.
     *
     * @param collection the collection of edges to fetch from
     * @return the list of edges
     */
    @GET
    @Path("{collection}/{filename}")
    @Produces("text/plain")
    public Response get(@PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection, @PathParam("filename") final String filename) {
        try {
            MongoDatabase applicationDatabase = MongoDBUtil.getApplicationDatabase(applicationId);
            GridFS gridFS = new GridFS(applicationDatabase.getDatabase(), collection);
            GridFSDBFile file = gridFS.findOne(filename);
            return Response.ok(file.getInputStream()).build();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (UnknownHostException | MongoAuthenticationException ex) {
            LOGGER.error("something is wrong", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
