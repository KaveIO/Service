/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * Filter created for returning CORS headers on OPTION calls.
 *
 * @author mhoekstra
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) throws IOException {

        if (request.getMethod().equals("OPTIONS")) {
            MultivaluedMap<String, Object> headers = response.getHeaders();

            headers.add("Access-Control-Allow-Origin", "*");

            String requestHeaders = request.getHeaderString("Access-Control-Request-Headers");
            if (requestHeaders != null) {
                headers.add("Access-Control-Allow-Headers", requestHeaders);
            }

            String requestMethod = request.getHeaderString("Access-Control-Request-Method");
            if (requestMethod != null) {
                headers.add("Access-Control-Allow-Methods", requestMethod);
            }

        }
    }
}