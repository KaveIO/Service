package nl.kpmg.af.service.security;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.kpmg.af.service.data.MongoDBUtil;
import nl.kpmg.af.service.data.security.User;
import nl.kpmg.af.service.data.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This {@link Filter} performs permission checking on requested resource. Authenticated client will have access to
 * resources,
 * that corresponds to his application name. For example, the client with username "wifi" will have access to resources
 * like this /Service/wifi/layer/visitDaily.
 *
 * @author Vladimir Kravtsov
 */
public class PermissionCheckerFilter implements Filter {

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

        ServiceRequest serviceRequest = createServiceRequest(httpServetRequest);
        User user = userRepository.findOneByUsername(userPrincipal.getName());

        if (serviceRequest.isValid() && user != null) {
            if (!user.isAllowed(serviceRequest)) {
                httpServetResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
            httpServetResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
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
}
