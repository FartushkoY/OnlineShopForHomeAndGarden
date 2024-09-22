package de.telran.onlineshopforhomeandgarden1.exception;

public class IllegalOperationInCartException extends NullPointerException{

    private String message;

    public IllegalOperationInCartException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
