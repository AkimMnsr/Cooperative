package fr.dlesaout.resdev.services;

import fr.dlesaout.resdev.entities.Categorie;
import fr.dlesaout.resdev.repositories.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<Categorie> searchAllCategories() {
        return categorieRepository.findAll();
    }

    public Optional<Categorie> searchCategorieById(Integer id) {
        return categorieRepository.findById(id);
    }

    public Optional<Categorie> saveCategorie(Categorie categorie) {
        Categorie newCategorie = categorieRepository.save(categorie);
        return categorieRepository.findById(newCategorie.getId());
    }

}
