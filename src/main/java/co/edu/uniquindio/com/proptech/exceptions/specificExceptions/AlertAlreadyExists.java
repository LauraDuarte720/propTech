package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class AlertAlreadyExists extends AlreadyExistingException {
    public AlertAlreadyExists(String field, String value) {
        super("The alert with " + field + ": " + value + " already exists");
    }
}
