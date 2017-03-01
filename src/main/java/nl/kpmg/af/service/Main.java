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

package nl.kpmg.af.service;

import nl.kpmg.af.service.server.Server;
import nl.kpmg.af.service.server.ServerGrizzly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by fziliotto on 24-6-16.
 */
public class Main {
  // Base URI the Grizzly HTTP server will listen on
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  /**
   * Main method.
   *
   * @param args
   */
  public static void main(String[] args) throws IOException {

    final Server server = new ServerGrizzly();
    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.out.println("Shutting down server...");

        server.stop();
      }
    });
  }
}
