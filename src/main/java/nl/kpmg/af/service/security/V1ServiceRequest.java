/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * v1/{service}/{applicationName}/{resource}
 */
public class V1ServiceRequest extends ServiceRequest {
    private final String service;
    private final String application;
    private final String resource;
    private final String operation;
    private boolean isValid = false;

    /**
     *
     * @param httpServetRequest
     */
    public V1ServiceRequest(HttpServletRequest httpServetRequest) {
        String pathInfo = httpServetRequest.getPathInfo();
        this.operation = httpServetRequest.getMethod();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 5
                    && pathParts[0].length() == 0
                    && pathParts[1].length() > 0
                    && pathParts[2].length() > 0
                    && pathParts[3].length() > 0
                    && pathParts[4].length() > 0) {

                service = pathParts[2];
                application = pathParts[3];
                resource = pathParts[4];
                isValid = true;
                return;
            }
        }

        service = "";
        application = "";
        resource = "";
    }

    /**
     *
     * @param requestContext
     */
    public V1ServiceRequest(ContainerRequestContext requestContext){
        String pathInfo = requestContext.getUriInfo().getPath(true);
        this.operation = requestContext.getMethod();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 4
                    && pathParts[0].length() > 0
                    && pathParts[1].length() > 0
                    && pathParts[2].length() > 0
                    && pathParts[3].length() > 0) {

                service = pathParts[1];
                application = pathParts[2];
                resource = pathParts[3];
                isValid = true;
                return;
            }
        }

        service = "";
        application = "";
        resource = "";
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    public String getService() {
        return service;
    }
}
