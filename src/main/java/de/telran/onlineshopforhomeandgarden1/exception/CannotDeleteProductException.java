package de.telran.onlineshopforhomeandgarden1.exception;

public class CannotDeleteProductException extends RuntimeException {

    private String message;
    public CannotDeleteProductException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
