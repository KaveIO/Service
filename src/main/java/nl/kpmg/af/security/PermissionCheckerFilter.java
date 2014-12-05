package nl.kpmg.af.security;

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

/**
 * This {@link Filter} performs permission checking on requested resource. Authenticated client will have access to
 * resources,
 * that corresponds to his application name. For example, the client with username "wifi" will have access to resources
 * like this /Service/wifi/layer/visitDaily.
 *
 * @author Vladimir Kravtsov
 */
public class PermissionCheckerFilter implements Filter {
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

        String username = userPrincipal.getName();
        String requestedPath = httpServetRequest.getPathInfo(); // assumed that path is in the format
                                                                // /{applicationName}/{serviceName}/{collection}

        String[] pathParts = requestedPath.split("/");
        if (pathParts.length >= 2) { // if path not equals to "/"
            String applicationName = pathParts[1];
            if (!username.equals(applicationName)) {
                httpServetResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
