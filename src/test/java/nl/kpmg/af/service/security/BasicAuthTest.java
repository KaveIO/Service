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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Created by fziliotto on 27-6-16.
 */
public class BasicAuthTest {

  @Test
  public void decodeTest() throws Exception {
    String[] credential;

    String username = "admin";
    String password = "admin";
    String basicAuthString = "Basic YWRtaW46YWRtaW4="; // auth string with admin:admin
    credential = BasicAuth.decode(basicAuthString);
    assertEquals(credential[0], username);
    assertEquals(credential[1], password);

    String nullAuthString = null;
    credential = BasicAuth.decode(nullAuthString);
    assertNull(credential);

    String emptyAuthentication = "";
    credential = BasicAuth.decode(emptyAuthentication);
    assertNull(credential);

    String onlyUsernameAuth = "Basic YWRtaW4="; // admin in Base64
    credential = BasicAuth.decode(onlyUsernameAuth);
    assertEquals(credential[0], username);
    assertEquals(credential.length, 1);

    String onlyPasswordAuth = "OmFkbWlu";
    credential = BasicAuth.decode(onlyPasswordAuth);
    assertEquals(credential[1], password);
    assertEquals(credential[0], "");
  }
}
