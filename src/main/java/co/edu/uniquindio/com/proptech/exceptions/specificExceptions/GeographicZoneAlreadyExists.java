package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class GeographicZoneAlreadyExists extends ConflictException {
    public GeographicZoneAlreadyExists(String field, String value) {
        super("The geographiczone with " + field + ": " + value + " already exists");
    }
}
