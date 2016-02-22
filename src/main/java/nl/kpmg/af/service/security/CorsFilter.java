/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter created for returning CORS headers on OPTION calls.
 *
 * @author mhoekstra
 */
public class CorsFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(CorsFilter.class.getName());

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServetRequest = (HttpServletRequest) request;
        HttpServletResponse httpServetResponse = (HttpServletResponse) response;


        if (httpServetRequest.getMethod().equals("OPTIONS")) {
            String requestHeaders = httpServetRequest.getHeader("Access-Control-Request-Headers");
            if (requestHeaders != null) {
                httpServetResponse.setHeader("Access-Control-Allow-Headers", requestHeaders);
            }

            String requestMethod = httpServetRequest.getHeader("Access-Control-Request-Method");
            if (requestMethod != null) {
                httpServetResponse.setHeader("Access-Control-Allow-Methods", requestMethod);
            }

            httpServetResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServetResponse.setStatus(200);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
