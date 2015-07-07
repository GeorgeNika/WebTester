package ua.george_nika.webtester.errors;

/**
 * Created by George on 25.06.2015.
 */
public class WorkWithDataBaseRuntimeException extends RuntimeException {

    public WorkWithDataBaseRuntimeException(String message) {
        super(message);
    }

    public WorkWithDataBaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public WorkWithDataBaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

