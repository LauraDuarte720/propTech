package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class PropertyAlreadyExists extends ConflictException {
    public PropertyAlreadyExists(String field, String value) {
        super("The property with " + field + ": " + value + " already exists");
    }
}
