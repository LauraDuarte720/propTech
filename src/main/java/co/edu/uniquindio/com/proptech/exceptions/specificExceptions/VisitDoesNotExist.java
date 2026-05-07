package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class VisitDoesNotExist extends NotFoundException {
    public VisitDoesNotExist(String field, String value) {
        super("The visit with " + field + ": " + value + " does not exist");
    }
}
