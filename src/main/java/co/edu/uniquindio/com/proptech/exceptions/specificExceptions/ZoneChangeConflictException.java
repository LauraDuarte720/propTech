package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.domain.dtos.AffectedPropertyDto;
import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

import java.util.List;

public class ZoneChangeConflictException extends ConflictException {

    private final List<AffectedPropertyDto> affectedProperties;

    public ZoneChangeConflictException(
            String message,
            List<AffectedPropertyDto> affectedProperties
    ) {
        super(message);
        this.affectedProperties = affectedProperties;
    }

    public List<AffectedPropertyDto> getAffectedProperties() {
        return affectedProperties;
    }
}