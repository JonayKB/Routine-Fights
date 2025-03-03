package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
