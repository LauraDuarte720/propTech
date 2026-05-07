package co.edu.uniquindio.com.proptech.exceptions.generalExceptions;

public abstract class AlreadyExistingException extends RuntimeException{
    public AlreadyExistingException(String message){
        super(message);
    }
}