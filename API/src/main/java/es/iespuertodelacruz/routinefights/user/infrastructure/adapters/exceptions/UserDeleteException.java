package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

public class UserDeleteException extends RuntimeException {
    public UserDeleteException(String message) {
        super(message);
    }
}
