package fr.dlesaout.resdev.exceptions.utilisateur;

public class PasswordInputErrorException extends RuntimeException {

    public PasswordInputErrorException() {
        super("Le mot de passe saisi ne respecte pas la politique de sécurisation des mots de passe");
    }

}
