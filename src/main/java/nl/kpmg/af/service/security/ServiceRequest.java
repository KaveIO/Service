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

import nl.kpmg.af.service.data.core.Role;

/**
 *
 * @author mhoekstra
 */
public abstract class ServiceRequest {

  public abstract boolean isValid();

  public abstract String getApplication();

  public abstract String getResource();

  public abstract String getOperation();

  public abstract String getService();

  public boolean isAllowed(Role role) {
    if (this != null && this.isValid()) {

      if (role.getDeny() != null) {
        for (Role.Rule rule : role.getDeny()) {
          if (match(rule)) {
            return false;
          }
        }
      }

      if (role.getAllow() != null) {
        for (Role.Rule rule : role.getAllow()) {
          if (match(rule)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private boolean match(Role.Rule rule) {
    char operation;
    switch (this.getOperation()) {
      case "POST":
        operation = 'c';
        break;
      case "GET":
        operation = 'r';
        break;
      case "PUT":
        operation = 'u';
        break;
      case "DELETE":
        operation = 'd';
        break;
      default:
        operation = '?';
    }

    boolean applicationMatch = rule.getService().equals(this.getService());
    boolean resourceMatch =
        rule.getResource().equals("*") || rule.getResource().equals(this.getResource());
    boolean rightsMatch = rule.getRights().equals("*") || rule.getRights().contains("" + operation);

    return applicationMatch && resourceMatch && rightsMatch;
  }

}
