package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.NotFoundException;

public class NoAdminActionsToUndo extends NotFoundException {
    public NoAdminActionsToUndo(String message) {
        super(message);
    }
}
