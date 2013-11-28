package nl.kpmg.af.service.exception;

/**
 *
 * @author Hoekstra.Maarten
 */
public class ApplicationDatabaseConnectionException extends Exception {
    public ApplicationDatabaseConnectionException() {
    }

    public ApplicationDatabaseConnectionException(String message) {
        super(message);
    }

    public ApplicationDatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationDatabaseConnectionException(Throwable cause) {
        super(cause);
    }

    public ApplicationDatabaseConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
