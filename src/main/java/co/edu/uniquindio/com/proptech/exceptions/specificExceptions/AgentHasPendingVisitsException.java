package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentHasPendingVisitsException extends ConflictException {
    public AgentHasPendingVisitsException(String value) {
        super("The agent with id: " + value + " still has visits in this zone");
    }
}
