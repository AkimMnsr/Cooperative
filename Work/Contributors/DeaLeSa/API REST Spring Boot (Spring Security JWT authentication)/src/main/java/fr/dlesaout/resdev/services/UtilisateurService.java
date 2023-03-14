package fr.dlesaout.resdev.services;

import fr.dlesaout.resdev.entities.Role;
import fr.dlesaout.resdev.entities.Utilisateur;
import fr.dlesaout.resdev.exceptions.utilisateur.UserRegistrationFailedException;
import fr.dlesaout.resdev.exceptions.utilisateur.EmailAlreadyExistsException;
import fr.dlesaout.resdev.exceptions.utilisateur.PasswordInputErrorException;
import fr.dlesaout.resdev.exceptions.utilisateur.UserNotFoundException;
import fr.dlesaout.resdev.repositories.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final
    UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> getAll() {
        var utilisateurs = utilisateurRepository.findAll();
        if (!utilisateurs.isEmpty()) {
            return utilisateurs;
        } else {
            throw new UserNotFoundException("Aucun utilisateur trouvé.");
        }
    }

    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        var utilisateur = utilisateurRepository.findById(id);
        if(utilisateur.isPresent()) {
            return utilisateur;
        } else {
            throw new UserNotFoundException("L'utilisateur " + id + " n'existe pas.");
        }
    }

    public Optional<Utilisateur> getUtilisateurByEmail(Utilisateur utilisateur) {
        Optional<Utilisateur> newUtilisateur;
        try {
            newUtilisateur = utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail());
        } catch (RuntimeException e) {
            throw new RuntimeException("Aucun utilisateur enregistré avec cette adresse email.");
        }
        if (!utilisateur.getMotDePasse().equals(newUtilisateur.get().getMotDePasse())) {
            throw new RuntimeException("Erreur lors de la saisie du mot de passe.");
        }
        Session session = new MapSession();
        session.setAttribute("utilisateur", newUtilisateur);
        return newUtilisateur;
    }

    public ResponseEntity<Utilisateur> saveUtilisateur(Utilisateur utilisateur) {

        String motDePasse = utilisateur.getMotDePasse();

        Optional<Utilisateur> utilisateurExistant = utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail());
        if (utilisateurExistant.isPresent()) {
            throw new EmailAlreadyExistsException("Un compte a déjà été créé avec cette adresse email.");
        }

        String PATTERN_MOT_DE_PASSE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        if(motDePasse.length()<8 && !motDePasse.contains(PATTERN_MOT_DE_PASSE)) {
            throw new PasswordInputErrorException();
        }

        Utilisateur newUtilisateur = Utilisateur.builder()
                .pseudo(utilisateur.getPseudo())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .telephone(utilisateur.getTelephone())
                .rue(utilisateur.getRue())
                .codePostal(utilisateur.getCodePostal())
                .ville(utilisateur.getVille())
                .motDePasse(utilisateur.getMotDePasse())
                .role(Role.UTILISATEUR)
                .build();

        try {
            utilisateurRepository.save(newUtilisateur);
        } catch (Exception e) {
            System.out.println(newUtilisateur.toString());
            throw new UserRegistrationFailedException();
        }

        return ResponseEntity.ok(newUtilisateur);

    }

}
