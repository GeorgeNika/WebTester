package ua.george_nika.webtester.errors;

/**
 * Created by George on 22.06.2015.
 */
public class UserWrongInputException extends RuntimeException {

    public UserWrongInputException(String message) {
        super(message);
    }

    public UserWrongInputException(Throwable cause) {
        super(cause);
    }

    public UserWrongInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
