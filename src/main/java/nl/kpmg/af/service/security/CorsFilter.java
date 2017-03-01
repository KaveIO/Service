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

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * Filter created for returning CORS headers on OPTION calls.
 *
 * @author mhoekstra
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response)
      throws IOException {

    if (request.getMethod().equals("OPTIONS")) {
      MultivaluedMap<String, Object> headers = response.getHeaders();

      headers.add("Access-Control-Allow-Origin", "*");

      String requestHeaders = request.getHeaderString("Access-Control-Request-Headers");
      if (requestHeaders != null) {
        headers.add("Access-Control-Allow-Headers", requestHeaders);
      }

      String requestMethod = request.getHeaderString("Access-Control-Request-Method");
      if (requestMethod != null) {
        headers.add("Access-Control-Allow-Methods", requestMethod);
      }

    }
  }
}
