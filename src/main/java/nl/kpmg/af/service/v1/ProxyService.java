/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1;

import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Proxy;
import nl.kpmg.af.service.data.core.repository.ProxyRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.v1.proxy.ProxyRequest;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author mhoekstra
 */
@Service
@Path("v1/proxy/{applicationId}/{name}{noop: (/)?}{ext : ((?<=/)[\\w\\d\\=\\?\\.\\,\\+\\!\\_\\(\\)\\/#\\-\\~\\*\\$\\|\"]*)?}")
public class ProxyService {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class);


    @Autowired
    private MongoDBUtil mongoDBUtil;

    @GET
    public Response getProxy(
            @Context final Request request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name,
            @PathParam("ext") final String extension) {
        return executeProxy(request, applicationId, name, extension);
    }


    @POST
    public Response postProxy(
            @Context final Request request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name,
            @PathParam("ext") final String extension) {
        return executeProxy(request, applicationId, name, extension);
    }

    @DELETE
    public Response deleteProxy(
            @Context final Request request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name,
            @PathParam("ext") final String extension) {
        return executeProxy(request, applicationId, name, extension);
    }

    @PUT
    public Response putProxy(
            @Context final Request request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name,
            @PathParam("ext") final String extension) {
        return executeProxy(request, applicationId, name, extension);
    }

    private Response executeProxy(
            final Request request,
            final String applicationId,
            final String name,
            final String extension) {
        try {
            ProxyRepository proxyRepository = mongoDBUtil.getProxyRepository(applicationId);
            Proxy proxy = proxyRepository.findOneByName(name);

            // Add path extension to the proxy target
            if(proxy.isPathExtension()== true){
                proxy.setTarget(extendPath(proxy.getTarget(), extension));
                LOGGER.info("Proxy target string extended with: /{}", extension);
            }

            ProxyRequest proxyRequest = new ProxyRequest(request, proxy);

            if(proxyRequest.isRecursive()){
                return Response.status(Response.Status.FORBIDDEN).entity("Can't make a request to the proxy server from "+request.getRequestURI()+"!").build();
            }

            return proxyRequest.execute();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Application proxyRepository couldn't be created", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * The extension string obtained in the request doesn't start with the slash ('/') character while
     * the proxy target (from the repository) may or may not contain a trailing slash. This method check if
     *  there is a trailing slash and adds it if it is absent during the concatenation, so that the URL is well formed.
     *
     * @param target the original target url
     * @param extension the url path extension from the proxy service
     */
    public static String extendPath(String target, String extension){
        String newTarget = target;
        if(!target.endsWith("/")){
            newTarget+="/";
        }
        newTarget+=extension;
        return newTarget;
    }
}
