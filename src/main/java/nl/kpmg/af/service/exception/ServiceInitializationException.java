package nl.kpmg.af.service.exception;

/**
 *
 * @author Hoekstra.Maarten
 */
public class ServiceInitializationException extends RuntimeException {
    public ServiceInitializationException() {
    }

    public ServiceInitializationException(String message) {
        super(message);
    }

    public ServiceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceInitializationException(Throwable cause) {
        super(cause);
    }

    public ServiceInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
