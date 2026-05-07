package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class OperationDoesNotExist extends NotFoundException {
    public OperationDoesNotExist(String field, String value) {
        super("The operation with " + field + ": " + value + " does not exist");
    }
}
