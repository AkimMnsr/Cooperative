package fr.dlesaout.resdev.repositories;

import fr.dlesaout.resdev.entities.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Integer> {

    Etat findEtatByNom(String nom);

}
