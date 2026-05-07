package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class AlertDoesNotExist extends NotFoundException {
    public AlertDoesNotExist(String field, String value) {
        super("The alert with " + field + ": " + value + " does not exist");
    }
}
