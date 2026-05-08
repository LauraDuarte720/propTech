package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class NoAgentConfirmationException extends ConflictException {
    public NoAgentConfirmationException() {
        super("If you continue, this property will not have any agent associated and can not be put in sold");
    }
}
