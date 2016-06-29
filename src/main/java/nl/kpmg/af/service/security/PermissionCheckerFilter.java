/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and
 * confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.beans.factory.annotation.Autowired;

import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Role;
import nl.kpmg.af.service.data.core.repository.RoleRepository;
import nl.kpmg.af.service.data.security.repository.UserRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

/**
 * This {@link Filter} performs permission checking on requested resource. Authenticated client will have access to
 * resources, that corresponds to his application name. For example, the client with username "wifi" will have access to
 * resources like this /Service/wifi/layer/visitDaily.
 *
 * @author Vladimir Kravtsov
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class PermissionCheckerFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = Logger.getLogger(PermissionCheckerFilter.class.getName());

	@Autowired
	private MongoDBUtil mongoDBUtil;

	@Autowired
	private UserRepository userRepository;

	private ServiceRequest createServiceRequest(HttpServletRequest httpServetRequest) {
		ServiceRequest request = new V0ServiceRequest(httpServetRequest);
		if (!request.isValid()) {
			request = new V1ServiceRequest(httpServetRequest);
		}
		return request;
	}

	/**
	 * Helper method for retrieving the list of roles associated to an user.
	 * <p>
	 * Our login methods rely on the CatalinaBearerTokenAuthenticator. This is a mechanism which interprets an oAuth2
	 * bearer token for authentication and role based authorization. This functionality hides the roles associated to
	 * the identified userPrinciple through a SkeletonKeyPrincipal and a RequestFacade. In that case we have to get the
	 * correct roleSet from deep down the current SecurityContext.
	 *
	 * @param request
	 * @return the roles of the current UserPrincipal
	 * @throws UserRolesException
	 */
	private List<String> getUserRoles(HttpServletRequest request) throws UserRolesException {
		List userRoles;

		String authType = request.getAuthType();
		if (authType == null) {
			throw new UserRolesException("no AuthType");
		}
		if (authType.equals("BASIC")) {
			Principal userPrincipal = request.getUserPrincipal();
			if (GenericPrincipal.class.isAssignableFrom(userPrincipal.getClass())) {
				userRoles = Arrays.asList(((GenericPrincipal) userPrincipal).getRoles());
			} else {
				throw new UserRolesException();
			}
		} else {
			throw new UserRolesException("Unknown AuthType");
		}
		return userRoles;
	}

	private List<String> getUserRoles(UserSecurityContext securityContext) throws UserRolesException {
		List userRoles;

		String authType = securityContext.getAuthenticationScheme();
		if (authType == null) {
			throw new UserRolesException("no AuthType");
		}
		if (authType.equals("BASIC")) {
			GenericPrincipal userPrincipal = securityContext.getPrincipal();
				userRoles = Arrays.asList(userPrincipal.getRoles());
		} else {
			throw new UserRolesException("Unknown AuthType");
		}
		return userRoles;
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        UserSecurityContext securityContext;
        if(containerRequestContext.getSecurityContext() instanceof UserSecurityContext) {
            securityContext = (UserSecurityContext) containerRequestContext.getSecurityContext();
        }else{
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
			LOGGER.log(Level.WARNING, "Role retrieval failed for user", ex);
			return;
		}

		ServiceRequest serviceRequest = createServiceRequest(containerRequestContext);

		try {
			RoleRepository roleRepository = mongoDBUtil.getRoleRepository(serviceRequest.getApplication());
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
			LOGGER.log(Level.SEVERE, "", ex);
		}
		// No error and the user role is not allowed to access the resource
		throw new WebApplicationException(HttpServletResponse.SC_FORBIDDEN);
	}

	private ServiceRequest createServiceRequest(ContainerRequestContext servletRequest) {
		ServiceRequest request = new V0ServiceRequest(servletRequest);
		if (!request.isValid()) {
			request = new V1ServiceRequest(servletRequest);
		}
		return request;
	}

}
