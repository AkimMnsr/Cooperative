package fr.dlesaout.resdev.exceptions.ressource;

public class RessourceNotFoundException extends RuntimeException {
    public RessourceNotFoundException(String message) {
        super(message);
    }
}
