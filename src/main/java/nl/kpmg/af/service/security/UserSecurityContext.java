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

import com.sun.security.auth.UserPrincipal;

import nl.kpmg.af.service.data.security.User;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by fziliotto on 28-6-16.
 */
public class UserSecurityContext implements SecurityContext {

  private User user;
  private String scheme;
  private ServicePrincipal principal;

  public UserSecurityContext(User user, String scheme) {
    this.user = user;
    this.scheme = scheme;
    this.principal = new ServicePrincipal(user.getUsername(), user.getPassword(), user.getRoles(),
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

  public ServicePrincipal getPrincipal() {
    return principal;
  }
}
