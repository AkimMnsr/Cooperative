package fr.dlesaout.resdev.exceptions.utilisateur;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
