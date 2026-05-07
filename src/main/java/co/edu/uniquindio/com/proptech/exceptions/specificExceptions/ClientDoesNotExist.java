package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class ClientDoesNotExist extends NotFoundException {
    public ClientDoesNotExist(String field, String value) {
        super("The client with " + field + ": " + value + " does not exist");
    }
}
