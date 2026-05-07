package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class AgentAlreadyExists extends AlreadyExistingException {
    public AgentAlreadyExists(String field, String value) {
        super("The agent with " + field + ": " + value + " already exists");
    }
}
