package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}