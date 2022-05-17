package app.scrumifiedbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        EntityException EntityException = new EntityException(
                entityNotFoundException.getMessage(),
                notFound,
                LocalDateTime.now());

        return new ResponseEntity<>(EntityException, notFound);
    }

    @ExceptionHandler(EntityNotSaveException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerEntityNotSaveException(EntityNotSaveException entityNotSaveException) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;
        EntityException entityException = new EntityException(
                entityNotSaveException.getMessage(),
                notFound,
                LocalDateTime.now());

        return new ResponseEntity<>(entityException, notFound);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerInvalidArgumentException(InvalidArgumentException invalidArgumentException) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        EntityException entityException = new EntityException(
                invalidArgumentException.getMessage(),
                badRequest,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(entityException, badRequest);
    }
}
