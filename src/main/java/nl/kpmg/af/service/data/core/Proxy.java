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
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 *
 * @author mhoekstra
 */
@Document(collection = "proxy")
public class Proxy {
  private String name;
  private String target;

  @Field("methods_allowed")
  private List<String> methodsAllowed;

  @Field("disable_ssl")
  private boolean disableSSL;

  @Field("path_extension")
  private boolean pathExtension;

  private String username;
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public List<String> getMethodsAllowed() {
    return methodsAllowed;
  }

  public void setMethodsAllowed(List<String> methods_allowed) {
    this.methodsAllowed = methods_allowed;
  }

  public boolean isDisableSSL() {
    return disableSSL;
  }

  public void setDisableSSL(boolean disableSSL) {
    this.disableSSL = disableSSL;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isPathExtension() {
    return pathExtension;
  }

  public void setPathExtension(boolean pathExtension) {
    this.pathExtension = pathExtension;
  }
}
