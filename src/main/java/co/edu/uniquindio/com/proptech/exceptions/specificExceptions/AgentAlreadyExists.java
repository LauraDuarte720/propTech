package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentAlreadyExists extends ConflictException {
    public AgentAlreadyExists(String field, String value) {
        super("The agent with " + field + ": " + value + " already exists");
    }
}
