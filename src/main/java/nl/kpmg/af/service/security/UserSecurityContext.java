package nl.kpmg.af.service.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.apache.catalina.realm.GenericPrincipal;

import com.sun.security.auth.UserPrincipal;

import nl.kpmg.af.service.data.security.User;

/**
 * Created by fziliotto on 28-6-16.
 */
public class UserSecurityContext implements SecurityContext {
	private User user;
	private String scheme;
	private GenericPrincipal principal;

	public UserSecurityContext(User user, String scheme) {
		this.user = user;
		this.scheme = scheme;

		this.principal = new GenericPrincipal(user.getUsername(), user.getPassword(), user.getRoles(),
				new UserPrincipal(user.getUsername()));
	}

	@Override
	public Principal getUserPrincipal() {
		return this.principal.getUserPrincipal();
	}

	@Override
	public boolean isUserInRole(String s) {
		if (user.getRoles() != null) {
			return user.getRoles().contains(s);
		}
		return false;
	}

	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return this.scheme;
	}

	public GenericPrincipal getPrincipal() {
		return principal;
	}
}
