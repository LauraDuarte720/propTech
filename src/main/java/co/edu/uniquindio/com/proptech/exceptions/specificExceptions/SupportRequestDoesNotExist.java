package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class SupportRequestDoesNotExist extends NotFoundException {
    public SupportRequestDoesNotExist(String field, String value) {
        super("The support request with " + field + ": " + value + " does not exist");
    }
}
