package fr.dlesaout.resdev.repositories;

import fr.dlesaout.resdev.entities.Categorie;
import fr.dlesaout.resdev.entities.Etat;
import fr.dlesaout.resdev.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Integer> {

    List<Ressource> findAllByEtat(Etat etat);
    List<Ressource> findAllByCategoriesIn(List<Categorie> categories);
    Optional<Ressource> findRessourceByUrl(String url);
}
