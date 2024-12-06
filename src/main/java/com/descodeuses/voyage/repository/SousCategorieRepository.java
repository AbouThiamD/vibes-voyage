package com.descodeuses.voyage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.SousCategorie;

@Repository
@RepositoryRestResource
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long>{

}
