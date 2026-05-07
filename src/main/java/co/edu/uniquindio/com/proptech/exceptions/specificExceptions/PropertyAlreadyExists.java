package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class PropertyAlreadyExists extends AlreadyExistingException {
    public PropertyAlreadyExists(String field, String value) {
        super("The property with " + field + ": " + value + " already exists");
    }
}
