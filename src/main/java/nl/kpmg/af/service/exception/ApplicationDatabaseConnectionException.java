/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.exception;

/**
 * @author Hoekstra.Maarten
 */
public class ApplicationDatabaseConnectionException extends Exception {
    public ApplicationDatabaseConnectionException() {
    }

    public ApplicationDatabaseConnectionException(final String message) {
        super(message);
    }

    public ApplicationDatabaseConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApplicationDatabaseConnectionException(final Throwable cause) {
        super(cause);
    }

    public ApplicationDatabaseConnectionException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
