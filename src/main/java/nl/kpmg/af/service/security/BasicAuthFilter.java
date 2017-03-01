/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package nl.kpmg.af.service.security;

import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.security.User;
import nl.kpmg.af.service.data.security.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by fziliotto on 24-6-16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

  /**
   * The name of the http request header containing the authentication user.
   */
  public static final String BASIC_AUTHENTICATION_HEADER = "Authorization";
  /**
   * Class logger
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthFilter.class);

  @Autowired
  UserRepository userRepository;
  @Autowired
  MongoDBUtil mongoDBUtil;

  private String username = "";
  private String password = "";
  private String realm = "DataAnalyticsOrganizationRealm";

  /**
   * Apply the filter : check input request, validate or not with user auth
   *
   * @param containerRequest The request from Tomcat server
   */
  @Override
  public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

    String method = containerRequest.getMethod();

    if (method.equals("OPTIONS")) {
      return;
    }

    // Get the authentification passed in HTTP headers parameters
    String auth = containerRequest.getHeaderString(BASIC_AUTHENTICATION_HEADER);
    // If the user does not have the right (does not provide any HTTP Basic Auth)
    if (auth == null || auth.equals("")) {
      throw new WebApplicationException(challengeResponse("Authorization required", "").build());
    }

    String[] credentials;
    // credentials[0] is the username, credentials[1] is the password
    try {
      credentials = BasicAuth.decode(auth);
    } catch (Exception ex) {
      throw new WebApplicationException(challengeResponse("Authorization required", "").build());
    }

    // If login or password fail
    if (credentials == null || credentials.length != 2) {
      throw new WebApplicationException(challengeResponse("Authorization required", "").build());
    }
    LOGGER.debug("Filtering request with basic authentication. Input username:password = {}:{}",
        credentials[0], credentials[1]);

    User user = userRepository.findOneByUsername(credentials[0]);
    if (user == null) {
      throw new WebApplicationException(challengeResponse("User does not exist", "").build());
    } else if (user.getPassword().equals(credentials[1])) {
      // We configure your Security Context here
      String scheme = containerRequest.getUriInfo().getRequestUri().getScheme();
      containerRequest.setSecurityContext(new UserSecurityContext(user, "BASIC"));
      return;
    } else {
      throw new WebApplicationException(challengeResponse("Wrong Password", "").build());
    }
  }

  /**
   * Method to for modifying the response object with an access denied message.
   *
   * @param error
   * @param description
   * @return always false allows for sweet one-liner on a challenge
   */
  protected Response.ResponseBuilder challengeResponse(String error, String description) {
    Response.ResponseBuilder builder = null;
    builder = Response.status(Response.Status.UNAUTHORIZED).entity(error);

    StringBuilder header = new StringBuilder();

    header.append("Basic realm=\"");
    header.append(realm).append("\"");

    if (error != null) {
      header.append(", error=\"").append(error).append("\"");
    }
    if (description != null) {
      header.append(", error_description=\"").append(description).append("\"");
    }
    builder.header("WWW-Authenticate", header.toString());

    return builder;
  }

}
