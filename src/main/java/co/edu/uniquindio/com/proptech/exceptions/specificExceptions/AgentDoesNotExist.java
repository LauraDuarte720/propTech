package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class AgentDoesNotExist extends NotFoundException {
    public AgentDoesNotExist(String field, String value) {
        super("The agent with " + field + ": " + value + " does not exist");
    }
}
