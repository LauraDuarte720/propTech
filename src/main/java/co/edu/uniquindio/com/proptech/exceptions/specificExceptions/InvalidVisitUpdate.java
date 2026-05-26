package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class InvalidVisitUpdate extends ConflictException {
    public InvalidVisitUpdate(VisitStatus status, String message) {
        super(message);
    }
}
