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
import nl.kpmg.af.service.data.core.Role;
import nl.kpmg.af.service.data.core.repository.RoleRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class PermissionCheckerFilter implements ContainerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(PermissionCheckerFilter.class);

  @Autowired
  private MongoDBUtil mongoDBUtil;

  /**
   * Retrieves the user roles from the security context
   *
   * @param securityContext
   * @return the roles of the current UserPrincipal
   * @throws UserRolesException
   */
  private List<String> getUserRoles(UserSecurityContext securityContext) throws UserRolesException {
    List userRoles;

    String authType = securityContext.getAuthenticationScheme();
    if (authType == null) {
      throw new UserRolesException("no AuthType");
    }
    if (authType.equals("BASIC")) {
      ServicePrincipal principal = securityContext.getPrincipal();
      return principal.getRoles();
    } else {
      throw new UserRolesException("Unknown AuthType");
    }
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    String method = containerRequestContext.getMethod();
    if (method.equals("OPTIONS")) {
      return;
    }

    UserSecurityContext securityContext;

    if (containerRequestContext.getSecurityContext() instanceof UserSecurityContext) {
      securityContext = (UserSecurityContext) containerRequestContext.getSecurityContext();
    } else {
      LOGGER.info("Error retrivieng the security context");
      throw new WebApplicationException(HttpServletResponse.SC_FORBIDDEN);
    }

    Principal userPrincipal = securityContext.getUserPrincipal();
    if (userPrincipal == null) {
      return;
    }

    List<String> userRoles;
    try {
      userRoles = getUserRoles(securityContext);
    } catch (UserRolesException ex) {
      LOGGER.warn("Role retrieval failed for user: {}", ex);
      return;
    }

    ServiceRequest serviceRequest = createServiceRequest(containerRequestContext);

    try {
      RoleRepository roleRepository =
          mongoDBUtil.getRoleRepository(serviceRequest.getApplication());
      Iterable<Role> allRoles = roleRepository.findAll();

      if (serviceRequest.isValid()) {
        for (Role role : allRoles) {
          if (userRoles.contains(serviceRequest.getApplication() + "." + role.getName())
              || userRoles.contains(role.getName())) {

            if (serviceRequest.isAllowed(role)) {
              return;
            }
          }
        }
      }
    } catch (ApplicationDatabaseConnectionException ex) {
      LOGGER.error("Database connection error: {}", ex);
    }
    // No error and the user role is not allowed to access the resource
    throw new WebApplicationException(HttpServletResponse.SC_FORBIDDEN);
  }

  private ServiceRequest createServiceRequest(ContainerRequestContext servletRequest) {
    return new V1ServiceRequest(servletRequest);
  }
}
