package es.iespuertodelacruz.routinefights.shared.exceptions;

public class DeeplException extends RuntimeException {
    public DeeplException(String message) {
        super(message);
    }

    public DeeplException(String message, Throwable cause) {
        super(message, cause);
    }
}
