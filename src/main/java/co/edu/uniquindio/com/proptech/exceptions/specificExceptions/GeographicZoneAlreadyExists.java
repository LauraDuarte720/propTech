package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.AlreadyExistingException;

public class GeographicZoneAlreadyExists extends AlreadyExistingException {
    public GeographicZoneAlreadyExists(String field, String value) {
        super("The geographiczone with " + field + ": " + value + " already exists");
    }
}
