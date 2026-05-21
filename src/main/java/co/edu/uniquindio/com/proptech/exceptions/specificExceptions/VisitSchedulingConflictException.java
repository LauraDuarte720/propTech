package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

import java.time.LocalDateTime;

public class VisitSchedulingConflictException extends ConflictException {
    public VisitSchedulingConflictException(String agentCedula, LocalDateTime date) {
        super("Agent " + agentCedula + " already has a visit scheduled near " + date + ". Visits must be at least 1 hour apart.");
    }
}