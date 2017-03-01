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

package nl.kpmg.af.service.v1.types;

/**
 *
 * @author mhoekstra
 */
public class PaginationException extends Exception {

  public PaginationException() {}

  public PaginationException(String message) {
    super(message);
  }

  public PaginationException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public PaginationException(Throwable cause) {
    super(cause);
  }

  public PaginationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }
}
