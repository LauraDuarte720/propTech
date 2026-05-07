package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class GeographicZoneDoesNotExist extends NotFoundException {
    public GeographicZoneDoesNotExist(String field, String value) {
        super("The geographiczone with " + field + ": " + value + " does not exist");
    }
}
