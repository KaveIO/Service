/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import javax.servlet.http.HttpServletRequest;

/**
 * /{applicationName}/{serviceName}/{collection}
 */
public class V0ServiceRequest extends ServiceRequest {
    private final String application;
    private final String resource;
    private final String operation;
    private final String service = "data";
    private boolean isValid = false;

    /**
     *
     * @param httpServetRequest
     */
    public V0ServiceRequest(HttpServletRequest httpServetRequest) {
        String pathInfo = httpServetRequest.getPathInfo();
        this.operation = httpServetRequest.getMethod();
        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 4
                    && pathParts[0].length() == 0
                    && pathParts[1].length() > 0
                    && pathParts[2].length() > 0
                    && pathParts[3].length() > 0) {

                application = pathParts[1];
                String resourceSuffix = pathParts[2].substring(0, 1).toUpperCase() + pathParts[2].substring(1).toLowerCase();
                resource = pathParts[3] + resourceSuffix;
                isValid = true;
                return;
            }
        }
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

    @Override
    public String getService() {
        return service;
    }

}
