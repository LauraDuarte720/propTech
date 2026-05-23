package co.edu.uniquindio.com.proptech.exceptions.generalExceptions;

public abstract class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}