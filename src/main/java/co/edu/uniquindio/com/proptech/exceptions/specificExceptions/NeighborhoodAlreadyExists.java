package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class NeighborhoodAlreadyExists extends ConflictException {
    public NeighborhoodAlreadyExists(String field, String value) {
        super("The neighborhood with " + field + ": " + value + " already exists");
    }
}
