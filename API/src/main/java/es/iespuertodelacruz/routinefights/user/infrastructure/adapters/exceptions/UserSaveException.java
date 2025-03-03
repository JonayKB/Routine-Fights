package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

public class UserSaveException extends RuntimeException {
    public UserSaveException(String message) {
        super(message);
    }
}
