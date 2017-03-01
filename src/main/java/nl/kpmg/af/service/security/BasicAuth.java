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

import javax.xml.bind.DatatypeConverter;

/**
 * Created by fziliotto on 24-6-16. Decodes a HTTP Basic auth string encoded in BASE 64
 */
public class BasicAuth {

  /**
   * Decode the basic auth and convert it to array login/password
   *
   * @param auth The string encoded
   * @return The login (case 0), the password (case 1)
   */
  public static String[] decode(String auth) {
    if (auth == null) {
      return null;
    }
    // Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
    auth = auth.replaceFirst("[B|b]asic ", "");

    // Decode the Base64 into byte[]
    byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

    // If the decode fails in any case
    if (decodedBytes == null || decodedBytes.length == 0) {
      return null;
    }

    // Now we can convert the byte[] into a splitted array :
    // - the first one is login,
    // - the second one password
    return new String(decodedBytes).split(":", 2);
  }

  private BasicAuth() {}
}
