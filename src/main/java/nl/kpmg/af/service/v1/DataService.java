/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1;

import nl.kpmg.af.service.v1.types.MeasurementFilter;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.data.core.repository.MeasurementRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.exception.InvalidRequestException;
import nl.kpmg.af.service.v1.types.MeasurementsRepresentation;
import nl.kpmg.af.service.v1.types.QueryCastException;
import nl.kpmg.af.service.v1.types.SortCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 *
 * @author roel
 */
@Service
@Path("v1/data/{applicationId}/{collection}")
public class DataService {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private MongoDBUtil mongoDBUtil;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response saveData(
            @Context final UriInfo uriInfo,
            @PathParam("applicationId") final String applicationId,
            @PathParam("collection") final String collection,
            Collection data) {
        try {
            MeasurementRepository repository = mongoDBUtil.getRepository(applicationId, collection);
            List<Measurement> measurements = getMeasurements(data);
            repository.save(measurements);
            return Response.status(Response.Status.OK).build();
        }
        catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Error has occured. The application database could not be connected.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (QueryCastException ex) {
            LOGGER.warn("Erroneous query content detected", ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("Problem detected in query request, this incident is logged.").build();
        }
    }

    private List<Measurement> getMeasurements(Collection data) throws QueryCastException {
        List<Measurement> measurements = new ArrayList<Measurement>();
        for (Object record : data) {
            if (Collection.class.isAssignableFrom(record.getClass())) {
                List<Measurement> ms = getMeasurements((Collection)record);
                measurements.addAll(ms);
            }
            else if (Map.class.isAssignableFrom(record.getClass())) {
                Measurement m = createMeasurement((Map)record);
                measurements.add(m);
            }
            else {
                throw new QueryCastException();
            }
        }
        return measurements;
    }

    private Measurement createMeasurement(Map record) {
        Measurement m = new Measurement();
        m.setMeasurementTimestamp(new Date());
        m.setVersion(2);
        m.putAll(record);
        return m;
    }
}