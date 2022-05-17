package app.scrumifiedbackend.exception;

public class EntityNotSaveException extends RuntimeException{
    public EntityNotSaveException(String message) {
        super(message);
    }
}
