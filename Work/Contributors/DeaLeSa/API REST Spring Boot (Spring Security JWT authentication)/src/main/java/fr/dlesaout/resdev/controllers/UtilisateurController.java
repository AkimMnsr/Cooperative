package fr.dlesaout.resdev.controllers;

import fr.dlesaout.resdev.entities.Utilisateur;
import fr.dlesaout.resdev.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/utilisateurs", name = "utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(
            UtilisateurService utilisateurService
    ) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping(value = "/all", name = "_all")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        var utilisateurs = utilisateurService.getAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping(value = "/{id}", name = "_detail")
    public ResponseEntity<Utilisateur> getUtilisateurById(
            @PathVariable Integer id
    ) {
        var utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur.get());
    }

    @PostMapping(value = "/create", name = "_create")
    public ResponseEntity<Utilisateur> saveUtilisateur(
            @RequestBody Utilisateur utilisateur
    ) {
        var newUtilisateur = utilisateurService.saveUtilisateur(utilisateur).getBody();
        return ResponseEntity.ok(newUtilisateur);
    }

}

