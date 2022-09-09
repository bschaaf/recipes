package nl.abnamro.cooking.controller.exceptions;

@SuppressWarnings("serial") 
public class EntityNotDeletedException extends RuntimeException {
    public EntityNotDeletedException(String message) {
        super(message);
    }
}
