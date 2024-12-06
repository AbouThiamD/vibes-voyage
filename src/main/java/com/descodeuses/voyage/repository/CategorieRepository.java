package com.descodeuses.voyage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.Categorie;

@Repository
// @RepositoryRestResource
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    public Boolean existsByNomCategorie(String nomCategorie);
}
