package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class ClientAlreadyExists extends ConflictException {
    public ClientAlreadyExists(String field, String value) {
        super("The client with " + field + ": " + value + " already exists");
    }
}
