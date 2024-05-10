package main.exceptions;

public class UserBadRequestException extends RuntimeException{
    public UserBadRequestException(String message) {
        super(message);
    }
}
