package main.handlers;

import main.exceptions.UserBadRequestException;
import main.exceptions.UserAlreadyExistsException;
import main.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        JsonResponse response = new JsonResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(UserBadRequestException.class)
    public ResponseEntity<?> handleUserAdditionFailedException(UserBadRequestException e) {
        JsonResponse response = new JsonResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        JsonResponse response = new JsonResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

