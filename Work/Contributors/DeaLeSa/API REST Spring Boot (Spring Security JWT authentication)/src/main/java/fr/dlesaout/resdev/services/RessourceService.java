package fr.dlesaout.resdev.services;

import fr.dlesaout.resdev.entities.*;
import fr.dlesaout.resdev.exceptions.ressource.BadRequestException;
import fr.dlesaout.resdev.exceptions.ressource.RessourceAlreadyExistsException;
import fr.dlesaout.resdev.exceptions.ressource.RessourceNotFoundException;
import fr.dlesaout.resdev.repositories.RessourceRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RessourceService {

    private final RessourceRepository ressourceRepository;

    public RessourceService(RessourceRepository ressourceRepository) {
        this.ressourceRepository = ressourceRepository;
    }

    public List<Ressource> searchAllRessources() {
        List<Ressource> ressources = ressourceRepository.findAll();
        if (ressources.isEmpty()) {
            throw new RessourceNotFoundException("Aucune ressource trouvée.");
        }
        return ressources;
    }

    public Optional<Ressource> searchRessourceById(Integer id) {
        Optional<Ressource> ressource = ressourceRepository.findById(id);
        if (ressource.isEmpty()) {
            throw new RessourceNotFoundException("La ressource " + id + " n'existe pas.");
        }
        return ressource;
    }

    public List<Ressource> searchRessourcesByEtat(Etat etat) {
        return ressourceRepository.findAllByEtat(etat);
    }

    public List<Ressource> searchRessourcesByCategories(List<Categorie> categories) {
        return ressourceRepository.findAllByCategoriesIn(categories);
    }

    @Transactional
    public Ressource saveRessource(Ressource ressource) {
        if (
                ressource == null
                        || ressource.getNom().length() > 255
                        || ressource.getUrl().length() > 255
                        || ressource.getDescription().length() > 1000
                        || ressource.getUtilisation().length() > 1000
        ) {
            throw new BadRequestException("Les données envoyées en paramètre ne respectent pas le format attendu.");
        }
        Utilisateur author = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Ressource newRessource = Ressource.builder()
                .nom(ressource.getNom())
                .url(ressource.getUrl())
                .description(ressource.getDescription())
                .utilisation(ressource.getUtilisation())
                .categories(ressource.getCategories())
                .etat(ressource.getEtat())
                .utilisateur(author)
                .build();
        Optional<Ressource> existingRessource = ressourceRepository.findRessourceByUrl(ressource.getUrl());
        if (existingRessource.isPresent()) {
            throw new RessourceAlreadyExistsException("Cette url est déjà associée à une ressource.");
        }
        ressourceRepository.save(newRessource);
        return newRessource;
    }

    @Transactional
    public Ressource updateRessource(Integer id, Ressource ressource) {
        Ressource ressourceToUpdate = ressourceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Les données envoyées en paramètre ne respectent pas le format attendu."));
        if (!ressourceToUpdate.getUtilisateur().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new BadRequestException("Les données envoyées en paramètre ne respectent pas le format attendu.");
        }
        ressourceToUpdate.setNom(ressource.getNom());
        ressourceToUpdate.setUrl(ressource.getUrl());
        ressourceToUpdate.setDescription(ressource.getDescription());
        ressourceToUpdate.setUtilisation(ressource.getUtilisation());
        ressourceToUpdate.setCategories(ressource.getCategories());
        ressourceRepository.findRessourceByUrl(ressource.getUrl())
                .ifPresent(r -> { throw new RessourceAlreadyExistsException("Cette url est déjà associée à une ressource."); });
        return ressourceRepository.save(ressourceToUpdate);
    }

    public Integer deleteRessourceById(Integer id) {
        try {
            ressourceRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

}
