package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentHasPendingSupportRequestsException extends ConflictException {
    public AgentHasPendingSupportRequestsException(String value) {
        super("The agent with id: " + value + " still has pending support requests");
    }
}