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

package nl.kpmg.af.service.data.core;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 *
 * @author mhoekstra
 */
@Document(collection = "roles")
public class Role {

  private String name;

  private List<Role.Rule> allow;

  private List<Role.Rule> deny;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Rule> getAllow() {
    return allow;
  }

  public void setAllow(List<Rule> allow) {
    this.allow = allow;
  }

  public List<Rule> getDeny() {
    return deny;
  }

  public void setDeny(List<Rule> deny) {
    this.deny = deny;
  }

  public static class Rule {

    private String service;

    private String resource;

    private String rights;

    public Rule(String service, String resource, String rights) {
      this.service = service;
      this.resource = resource;
      this.rights = rights;
    }

    public String getService() {
      return service;
    }

    public void setService(String service) {
      this.service = service;
    }

    public String getResource() {
      return resource;
    }

    public void setResource(String resource) {
      this.resource = resource;
    }

    public String getRights() {
      return rights;
    }

    public void setRights(String rights) {
      this.rights = rights;
    }
  }
}
