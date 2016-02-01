/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Proxy;
import nl.kpmg.af.service.data.core.repository.ProxyRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import nl.kpmg.af.service.v1.proxy.ProxyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mhoekstra
 */
@Service
@Path("v1/proxy/{applicationId}/{name}")
public class ProxyService {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class);

    @Autowired
    private MongoDBUtil mongoDBUtil;

    @GET
    public Response getProxy(
            @Context final HttpServletRequest request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name) {
        return executeProxy(request, applicationId, name);
    }

    @POST
    public Response postProxy(
            @Context final HttpServletRequest request,
            @PathParam("applicationId") final String applicationId,
            @PathParam("name") final String name) {
        return executeProxy(request, applicationId, name);
    }

    private Response executeProxy(
            final HttpServletRequest request,
            final String applicationId,
            final String name) {
        try {
            ProxyRepository proxyRepository = mongoDBUtil.getProxyRepository(applicationId);
            Proxy proxy = proxyRepository.findOneByName(name);

            ProxyRequest proxyRequest = new ProxyRequest(request, proxy);
            return proxyRequest.execute();
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.error("Application proxyRepository couldn't be created", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}