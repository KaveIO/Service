/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

import com.sun.security.auth.UserPrincipal;

import java.util.List;

/**
 *
 * @author mhoekstra
 */
class ServicePrincipal {

  private final String username;
  private final String password;
  private final List<String> roles;
  private final UserPrincipal userPrincipal;

  public ServicePrincipal(String username, String password, List<String> roles, UserPrincipal userPrincipal) {
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.userPrincipal = userPrincipal;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public List<String> getRoles() {
    return roles;
  }

  public UserPrincipal getUserPrincipal() {
    return userPrincipal;
  }
}
