package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentHasPendingOperationsException extends ConflictException {
    public AgentHasPendingOperationsException(String agentId) {
        super("The agent with id: " + agentId + " still has active operations for this property");
    }
}
