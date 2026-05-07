package co.edu.uniquindio.com.proptech.exceptions.generalExceptions;

public abstract class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
