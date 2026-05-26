package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentHasNoVisitsException extends ConflictException {
    public AgentHasNoVisitsException(String agentId) {
        super("Agent " + agentId + " has no scheduled visits");
    }
}