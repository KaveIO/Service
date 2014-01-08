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
