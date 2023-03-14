package fr.dlesaout.resdev.controllers;

import fr.dlesaout.resdev.entities.Categorie;
import fr.dlesaout.resdev.services.CategorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/categories", name = "categories")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping(value = "/all", name = "_all")
    public ResponseEntity<List<Categorie>> allCategories() {
        return ResponseEntity.ok(categorieService.searchAllCategories());
    }

    @GetMapping(value = "/{id}", name = "_detail")
    public ResponseEntity<Categorie> categorieById(@PathVariable Integer id) {
        Optional<Categorie> categorie = categorieService.searchCategorieById(id);
        return categorie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create", name = "_create")
    public ResponseEntity<Optional<Categorie>> saveCategorie(@RequestBody Categorie categorie) {
        Optional<Categorie> savedCategorie = categorieService.saveCategorie(categorie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategorie);
    }

}
