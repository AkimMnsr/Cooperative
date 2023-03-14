package fr.dlesaout.resdev.exceptions.utilisateur;

public class UserRegistrationFailedException extends RuntimeException {
    public UserRegistrationFailedException() {
        super("Un problème est survenu lors de l'enregistrement de l'utilisateur en base de données.");
    }
}
