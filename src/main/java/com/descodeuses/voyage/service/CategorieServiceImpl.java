package com.descodeuses.voyage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.repository.CategorieRepository;

@Service
public class CategorieServiceImpl implements CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Override
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    @Override
    public List<Categorie> getAllCategorie() {
        return categorieRepository.findAll();
    }

    @Override
    public Boolean existCategorie(String nomCategorie) {
        return categorieRepository.existsByNomCategorie(nomCategorie);
    }

    @Override
    public List<Categorie> findByNomCategorie(String nomCategorie) {
       return categorieRepository.findAll();
    }
    
   
    // @Override
    // public List<Categorie> findByNomDeCategorie(String nomCategorie) {
    //     return categorieRepository.findByNomDeCategorie(nomCategorie);
    // }

    @Override
    public Boolean deleteCategorie(long id) {
        Categorie categorie = categorieRepository.findById(id).orElse(null);
    
        if (!ObjectUtils.isEmpty(categorie)) {
            categorieRepository.delete(categorie);
            return true;
        }
        return false;
    }

    @Override
    public Categorie getCategorieById(long id) {
       Categorie categorie = categorieRepository.findById(id).orElse(null);
       return categorie;
    }
    
     
    
}


