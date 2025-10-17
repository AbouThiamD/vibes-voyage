package com.descodeuses.voyage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.Categorie;

@Repository
// @RepositoryRestResource
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    public Boolean existsByNomCategorie(String nomCategorie);
     Optional<Categorie> findByNomCategorieIgnoreCase(String nomCategorie);
}
