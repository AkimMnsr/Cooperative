package fr.dlesaout.resdev.exceptions.utilisateur;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
