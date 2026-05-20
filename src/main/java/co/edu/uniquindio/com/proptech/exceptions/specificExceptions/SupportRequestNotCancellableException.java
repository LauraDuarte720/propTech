package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

public class SupportRequestNotCancellableException extends RuntimeException {
    public SupportRequestNotCancellableException(String field, String value) {
        super("Support request with " + field + ": " + value + " is not cancellable");
    }
}