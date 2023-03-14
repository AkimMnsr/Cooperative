package fr.dlesaout.resdev.controllers;

import fr.dlesaout.resdev.entities.Etat;
import fr.dlesaout.resdev.services.EtatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/etats", name = "etats")
public class EtatController {

    private final EtatService etatService;

    public EtatController(EtatService etatService) {
        this.etatService = etatService;
    }

    @GetMapping(value = "/all", name = "_all")
    public ResponseEntity<List<Etat>> allEtats() {
        return ResponseEntity.ok(etatService.searchAllEtats());
    }

    @GetMapping(value = "/{id}", name = "_detail")
    public ResponseEntity<Etat> etatById(@PathVariable Integer id) {
        return ResponseEntity.ok(etatService.searchEtatById(id));
    }

    @PostMapping(value = "/create", name = "_create")
    public ResponseEntity<Optional<Etat>> saveCategorie(@RequestBody Etat etat) {
        Optional<Etat> savedEtat = etatService.saveEtat(etat);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEtat);
    }

}
