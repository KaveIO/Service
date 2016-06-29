package nl.kpmg.af.service.security;

import nl.kpmg.af.service.data.security.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by fziliotto on 28-6-16.
 */
public class UserSecurityContext implements SecurityContext{
        private User user;
        private String scheme;

        public UserSecurityContext(User user, String scheme) {
            this.user = user;
            this.scheme = scheme;
        }

        @Override
        public Principal getUserPrincipal() {return this.user;}

        @Override
        public boolean isUserInRole(String s) {
            if (user.getRoles() != null) {
                return user.getRoles().contains(s);
            }
            return false;
        }

        @Override
        public boolean isSecure() {return "https".equals(this.scheme);}

        @Override
        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }


}
