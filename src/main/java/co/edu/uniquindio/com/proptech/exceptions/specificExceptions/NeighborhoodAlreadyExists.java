package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class NeighborhoodAlreadyExists extends AlreadyExistingException {
    public NeighborhoodAlreadyExists(String field, String value) {
        super("The neighborhood with " + field + ": " + value + " already exists");
    }
}
