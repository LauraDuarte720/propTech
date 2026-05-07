package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class VisitAlreadyExists extends AlreadyExistingException {
    public VisitAlreadyExists(String field, String value) {
        super("The visit with " + field + ": " + value + " already exists");
    }
}
