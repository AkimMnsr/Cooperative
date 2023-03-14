package fr.dlesaout.resdev.exceptions.utilisateur;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
