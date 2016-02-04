/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.security;

/**
 *
 * @author mhoekstra
 */
public class UserRolesException extends Exception {

    public UserRolesException() {
    }

    public UserRolesException(String message) {
        super(message);
    }

    public UserRolesException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UserRolesException(Throwable cause) {
        super(cause);
    }

    public UserRolesException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
}
