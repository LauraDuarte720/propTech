package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

import java.util.ConcurrentModificationException;

public class NotNullOperationTypeException extends ConflictException {
    public NotNullOperationTypeException(String message) {
        super(message);
    }
}
