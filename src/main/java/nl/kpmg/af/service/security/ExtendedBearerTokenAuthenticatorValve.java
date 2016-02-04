/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import nl.kpmg.af.service.data.security.User;
import nl.kpmg.af.service.data.security.repository.UserRepository;
import org.apache.catalina.connector.Request;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.realm.GenericPrincipal;
import org.jboss.resteasy.skeleton.key.as7.BearerTokenAuthenticatorValve;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Extended version of the JBoss BearerTokenAuthenticatorValve which offers Basic Auth in front of oAuth.
 *
 * The basic authentication method of the Service is oAuth with a client credentials grant. This is preferred in
 * nearly all cases. However in some cases we do need to support Basic Auth as well. In this class the authenticate
 * method provided by BearerTokenAuthenticatorValve is wrapped in some logic facilitate Basic authentication. The
 * drawback is that Basic Auth needs to be evaluated on each call making this a potential performance bottle neck.
 *
 * @author mhoekstra
 */
@Component
public class ExtendedBearerTokenAuthenticatorValve extends BearerTokenAuthenticatorValve {

    @Override
    protected boolean authenticate(Request request, HttpServletResponse response, LoginConfig config) throws IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return challengeResponse(response, "", "");
        }

        String[] split = authHeader.trim().split("\\s+");
        if (split[0].equalsIgnoreCase("Basic")) {
            String tokenString = split[1];
            String authString = new String(DatatypeConverter.parseBase64Binary(tokenString));

            String[] authStringParts = authString.split(":");

            if (authStringParts.length != 2) {
                return challengeResponse(response, "", "");
            }

            String username = authStringParts[0];
            String password = authStringParts[1];

            if (username.length() == 0 || password.length() == 0 ) {
                return challengeResponse(response, "", "");
            }

            // Valves aren't executed in the actual application scope so Spring is not able to inject our
            // ApplicationContextFetch so we try to fetch this from our servlet context.
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
                    request.getContext().getServletContext());
            UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);

            User user = userRepository.findOneByUsername(username);
            if (user.getPassword().equals(password)) {
                GenericPrincipal principal = new GenericPrincipal(
                        request.getContext().getRealm(),
                        username,
                        password,
                        user.getRoles());

                request.setUserPrincipal(principal);
                request.setAuthType("BASIC");
                return true;
            }

            return challengeResponse(response, "", "");
        } else {
            // Fallback to oAuth via BearerTokenAuthenticatorValve
            return super.authenticate(request, response, config);
        }
    }

    /**
     * Method to for modifying the response object with an access denied message.
     *
     * @param response
     * @param error
     * @param description
     * @return always false allows for sweet one-liner on a challenge
     */
    protected boolean challengeResponse(HttpServletResponse response, String error, String description) {
        StringBuilder header = new StringBuilder("Bearer realm=\"");
        header.append(resourceMetadata.getRealm()).append("\"");
        if (error != null) {
            header.append(", error=\"").append(error).append("\"");
        }
        if (description != null) {
            header.append(", error_description=\"").append(description).append("\"");
        }
        response.setHeader("WWW-Authenticate", header.toString());
        try {
            response.sendError(401);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
