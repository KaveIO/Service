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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * v1/{service}/{applicationName}/{resource}
 */
public class V1ServiceRequest extends ServiceRequest {
  private final String service;
  private final String application;
  private final String resource;
  private final String operation;
  private boolean isValid = false;

  /**
   *
   * @param httpServetRequest
   */
  public V1ServiceRequest(HttpServletRequest httpServetRequest) {
    String pathInfo = httpServetRequest.getPathInfo();
    this.operation = httpServetRequest.getMethod();

    if (pathInfo != null) {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 5 && pathParts[0].length() == 0 && pathParts[1].length() > 0
          && pathParts[2].length() > 0 && pathParts[3].length() > 0 && pathParts[4].length() > 0) {

        service = pathParts[2];
        application = pathParts[3];
        resource = pathParts[4];
        isValid = true;
        return;
      }
    }

    service = "";
    application = "";
    resource = "";
  }

  /**
   *
   * @param requestContext
   */
  public V1ServiceRequest(ContainerRequestContext requestContext) {
    String pathInfo = requestContext.getUriInfo().getPath(true);
    this.operation = requestContext.getMethod();

    if (pathInfo != null) {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length >= 4 && pathParts[0].length() > 0 && pathParts[1].length() > 0
          && pathParts[2].length() > 0 && pathParts[3].length() > 0) {

        service = pathParts[1];
        application = pathParts[2];
        resource = pathParts[3];
        isValid = true;
        return;
      }
    }

    service = "";
    application = "";
    resource = "";
  }

  @Override
  public boolean isValid() {
    return isValid;
  }

  @Override
  public String getApplication() {
    return application;
  }

  @Override
  public String getResource() {
    return resource;
  }

  @Override
  public String getOperation() {
    return operation;
  }

  public String getService() {
    return service;
  }
}
