/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.types;

/**
 *
 * @author mhoekstra
 */
public class PaginationException extends Exception {

    public PaginationException() {
    }

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
