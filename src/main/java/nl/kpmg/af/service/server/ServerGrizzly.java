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

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URI;

/**
 * Created by fziliotto on 24-6-16.
 */
public class ServerGrizzly implements Server {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrizzly.class);
  ApplicationContext context;
  private HttpServer server;

  public ServerGrizzly() {}

  @Override
  public void start() {
    context = new ClassPathXmlApplicationContext(new String[] {"appConfig.xml"});
    AppConfig config = context.getBean(AppConfig.class);
    String baseUri = "http://" + config.getServerHost() + ":" + config.getServerPort();

    DataServiceApplication app = new DataServiceApplication(config.getServerName());

    server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), app);
  }

  @Override
  public void stop() {
    server.shutdown();
  }
}
