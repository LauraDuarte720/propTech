package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AlertAlreadyExists extends ConflictException {
    public AlertAlreadyExists(String field, String value) {
        super("The alert with " + field + ": " + value + " already exists");
    }
}
