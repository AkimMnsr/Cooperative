package fr.dlesaout.resdev.controllers;

import fr.dlesaout.resdev.entities.Categorie;
import fr.dlesaout.resdev.entities.Etat;
import fr.dlesaout.resdev.entities.Ressource;
import fr.dlesaout.resdev.services.RessourceService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/ressources", name = "ressources")
public class RessourceController {

    private final RessourceService ressourceService;

    public RessourceController(
            RessourceService ressourceService
    ) {
        this.ressourceService = ressourceService;
    }

    @GetMapping(value = "/all", name = "_all")
    public ResponseEntity<List<Ressource>> getAllRessources() {
        return Optional.ofNullable(ressourceService.searchAllRessources())
                .map(ressources -> new ResponseEntity<>(ressources, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/{id}", name = "_detail")
    public ResponseEntity<Optional<Ressource>> getRessourceById(@PathVariable Integer id) {
        return Optional.ofNullable(ressourceService.searchRessourceById(id))
                .map(ressource -> new ResponseEntity<>(ressource, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/etat", name = "_etat")
    public ResponseEntity<List<Ressource>> getRessourcesByEtat(@RequestParam Etat etat) {
        return Optional.ofNullable(ressourceService.searchRessourcesByEtat(etat))
                .map(ressources -> new ResponseEntity<>(ressources, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/categories", name = "_categories")
    public ResponseEntity<List<Ressource>> getRessourcesByCategories(@RequestParam List<Categorie> categories) {
        return Optional.ofNullable(ressourceService.searchRessourcesByCategories(categories))
                .map(ressources -> new ResponseEntity<>(ressources, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping(value = "/create", name = "_create")
    public ResponseEntity<Ressource> saveRessource(@RequestBody Ressource ressource) {
        return ResponseEntity.ok(ressourceService.saveRessource(ressource));
    }

    @PostMapping(value = "/update/{id}", name = "_update")
    public ResponseEntity<Ressource> updateRessource(@PathVariable Integer id, @RequestBody Ressource ressource) {
        return ResponseEntity.ok(ressourceService.updateRessource(id, ressource));
    }

    @PostMapping(value = "/delete/{id}", name = "_delete")
    public ResponseEntity<Integer> updateRessource(@PathVariable Integer id) {
        return ResponseEntity.ok(ressourceService.deleteRessourceById(id));
    }

    /*
    Format de fichier .xls ou .xlsx à importer
    | Catégorie | Nom | URL | Description | Utilisation |
    | ----------| --- | --- | ----------- | ----------- |
    | xxx       | xxx | xxx | xxx         | xxx         |
    | xxx       | xxx | xxx | xxx         | xxx         |
     */
    @PostMapping(value = "/import-file", name = "_import-file")
    public ResponseEntity<List<Ressource>> importRessources(@RequestParam("file") MultipartFile file) {
        try {
            List<Ressource> ressources = new ArrayList<>();

            // Utilise Apache POI pour lire le contenu du fichier Excel
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Itère sur les lignes du fichier Excel et crée une ressource pour chaque ligne
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Ressource ressource = Ressource.builder()
                        .nom(row.getCell(1).getStringCellValue())
                        .url(row.getCell(2).getStringCellValue())
                        .description(row.getCell(3).getStringCellValue())
                        .utilisation(row.getCell(4).getStringCellValue())
                        .build();
                System.out.println(ressource);

                // Enregistre la ressource dans la base de données en utilisant le service approprié
                ressourceService.saveRessource(ressource);
                ressources.add(ressource);
            }

            return ResponseEntity.ok(ressources);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
