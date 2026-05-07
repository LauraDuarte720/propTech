package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class OperationAlreadyExists extends ConflictException {
    public OperationAlreadyExists(String field, String value) {
        super("The operation with " + field + ": " + value + " already exists");
    }
}
