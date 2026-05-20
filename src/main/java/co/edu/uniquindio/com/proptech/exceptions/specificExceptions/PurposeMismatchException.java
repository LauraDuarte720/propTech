package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class PurposeMismatchException extends ConflictException {
    public PurposeMismatchException(String message) {
        super(message);
    }
}
