package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class InvalidVisitTransitionException extends ConflictException {
    public InvalidVisitTransitionException(VisitStatus current, VisitStatus next, String reason) {
        super("Invalid transition from " + current + " to " + next + ": " + reason);
    }
}