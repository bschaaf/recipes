package nl.abnamro.cooking.controller;

import nl.abnamro.cooking.controller.exceptions.EntityNotDeletedException;
import nl.abnamro.cooking.controller.exceptions.EntityNotFoundException;
import nl.abnamro.cooking.controller.mapping.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for exceptions occurring in the Cooking Controller
 */
@RestControllerAdvice
public class CookingControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse entityNotFoundException(EntityNotFoundException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(EntityNotDeletedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse entityNotDeletedException(EntityNotDeletedException exception) {
        return new MessageResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponse genericException(Exception exception) {
        return new MessageResponse(exception.getMessage());
    }
}
