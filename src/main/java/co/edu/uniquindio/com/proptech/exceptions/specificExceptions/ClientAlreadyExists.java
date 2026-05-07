package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

public class ClientAlreadyExists extends RuntimeException {
    public ClientAlreadyExists(String field, String value) {
        super("The client with " + field + ": " + value + " already exists");
    }
}
