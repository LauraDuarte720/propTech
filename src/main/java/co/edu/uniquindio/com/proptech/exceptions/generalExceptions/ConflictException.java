package co.edu.uniquindio.com.proptech.exceptions.generalExceptions;

public abstract class ConflictException extends RuntimeException{
    public ConflictException(String message){
        super(message);
    }
}