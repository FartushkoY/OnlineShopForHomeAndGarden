package de.telran.onlineshopforhomeandgarden1.exception;

public class CannotDeleteOrderException extends RuntimeException {

   private String message;

    public CannotDeleteOrderException(String message) {
    this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
