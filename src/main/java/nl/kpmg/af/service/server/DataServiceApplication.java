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

package nl.kpmg.af.service.server;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import nl.kpmg.af.service.security.BasicAuthFilter;
import nl.kpmg.af.service.security.CorsFilter;
import nl.kpmg.af.service.security.PermissionCheckerFilter;

/**
 * Created by fziliotto on 29-6-16.
 */
public class DataServiceApplication extends ResourceConfig {

  public DataServiceApplication() {
    init();
  }

  public DataServiceApplication(String name) {
    this.setApplicationName(name);
    init();
  }

  private void init() {
    packages(true, "nl.kpmg.af.service.v0");
    packages(true, "nl.kpmg.af.service.v1");
    register(BasicAuthFilter.class);
    register(PermissionCheckerFilter.class);
    register(CorsFilter.class);
    registerClasses(JacksonFeature.class);
    registerClasses(JacksonJsonProvider.class);
  }
}
