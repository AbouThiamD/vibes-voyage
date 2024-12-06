package com.descodeuses.voyage.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.descodeuses.voyage.model.Categorie;

public interface CategorieService {

    public Categorie saveCategorie(Categorie categorie);

    public Boolean existCategorie(String name);

    public List<Categorie> getAllCategorie();

    public List<Categorie> findByNomCategorie(String nomCategorie);

    public Boolean deleteCategorie (long  id);
    
    public Categorie getCategorieById (long id);
}




