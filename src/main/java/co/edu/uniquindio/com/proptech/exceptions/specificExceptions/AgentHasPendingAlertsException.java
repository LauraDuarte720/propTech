package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class AgentHasPendingAlertsException extends ConflictException {
    public AgentHasPendingAlertsException(String agentId, String propertyCode) {
        super("The agent " + agentId + " still has pending alerts for property " + propertyCode);
    }
}
