/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.core.Role;
import nl.kpmg.af.service.data.core.repository.RoleRepository;
import nl.kpmg.af.service.data.security.repository.UserRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;
import org.apache.catalina.realm.GenericPrincipal;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SubjectInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This {@link Filter} performs permission checking on requested resource. Authenticated client will have access to
 * resources, that corresponds to his application name. For example, the client with username "wifi" will have access to
 * resources like this /Service/wifi/layer/visitDaily.
 *
 * @author Vladimir Kravtsov
 */
public class PermissionCheckerFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(PermissionCheckerFilter.class.getName());

    @Autowired
    private MongoDBUtil mongoDBUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServetRequest = (HttpServletRequest) request;
        HttpServletResponse httpServetResponse = (HttpServletResponse) response;

        Principal userPrincipal = httpServetRequest.getUserPrincipal();
        if (userPrincipal == null) {
            httpServetResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<String> userRoles;
        try {
            userRoles = getUserRoles((HttpServletRequest) request);
        } catch (UserRolesException ex) {
            LOGGER.log(Level.WARNING, "Role retrieval failed for user", ex);
            httpServetResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ServiceRequest serviceRequest = createServiceRequest(httpServetRequest);
        try {
            RoleRepository roleRepository = mongoDBUtil.getRoleRepository(serviceRequest.getApplication());
            Iterable<Role> allRoles = roleRepository.findAll();

            if (serviceRequest.isValid()) {
                for (Role role : allRoles) {
                    if (userRoles.contains(serviceRequest.getApplication() + "." + role.getName())
                            || userRoles.contains(role.getName())) {

                        if (serviceRequest.isAllowed(role)) {
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                }
            }
        } catch (ApplicationDatabaseConnectionException ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            throw new ServletException(ex);
        }

        httpServetResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void destroy() {

    }

    private ServiceRequest createServiceRequest(HttpServletRequest httpServetRequest) {
        ServiceRequest request = new V0ServiceRequest(httpServetRequest);
        if (!request.isValid()) {
            request = new V1ServiceRequest(httpServetRequest);
        }
        return request;
    }

    /**
     * Helper method for retrieving the list of roles associated to an user.
     *
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
        List userRoles = new LinkedList();

        String authType = request.getAuthType();
        if (authType == null) {
            throw new UserRolesException("no AuthType");
        }

        if (authType.equals("OAUTH_BEARER")) {
            SecurityContext securityContext = SecurityContextAssociation.getSecurityContext();
            if (securityContext != null) {
                SubjectInfo subjectInfo = securityContext.getSubjectInfo();

                Subject authenticatedSubject = subjectInfo.getAuthenticatedSubject();
                Set<SimpleGroup> principals = authenticatedSubject.getPrincipals(SimpleGroup.class);

                for (SimpleGroup principal : principals) {
                    if (principal.getName().equals("Roles")) {
                        Enumeration<Principal> members = principal.members();
                        while (members.hasMoreElements()) {
                            Principal member = members.nextElement();
                            userRoles.add(member.getName());
                        }
                    }
                }
            }
        } else if (authType.equals("BASIC")){
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
}
