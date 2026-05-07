package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class NeighborhoodDoesNotExist extends NotFoundException {
    public NeighborhoodDoesNotExist(String field, String value) {
        super("The neighborhood with " + field + ": " + value + " does not exist");
    }
}
