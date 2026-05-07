package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class OperationAlreadyExists extends AlreadyExistingException {
    public OperationAlreadyExists(String field, String value) {
        super("The operation with " + field + ": " + value + " already exists");
    }
}
