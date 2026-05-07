package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class VisitAlreadyExists extends ConflictException {
    public VisitAlreadyExists(String field, String value) {
        super("The visit with " + field + ": " + value + " already exists");
    }
}
