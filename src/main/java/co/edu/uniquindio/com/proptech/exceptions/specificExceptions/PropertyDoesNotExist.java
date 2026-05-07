package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class PropertyDoesNotExist extends NotFoundException {
    public PropertyDoesNotExist(String field, String value) {
        super("The property with " + field + ": " + value + " does not exist");
    }
}
