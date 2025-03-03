package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message) {
        super(message);
    }
}
