package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;


import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class PropertyNotOperatableException extends ConflictException {
    public PropertyNotOperatableException(String code, PropertyStatus status) {
        super("Property " + code + " cannot be operated because it is " + status);
    }
}