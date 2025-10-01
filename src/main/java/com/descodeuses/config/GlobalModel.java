package com.descodeuses.config;

import java.util.List;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.data.domain.Sort;

import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.repository.CategorieRepository;

@ControllerAdvice
public class GlobalModel {

    private final CategorieRepository categorieRepository;

    public GlobalModel(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

      @ModelAttribute("categories")
    public List<Categorie> categories() {
        return categorieRepository.findAll(
            Sort.by(Sort.Order.by("nomCategorie").ignoreCase())
        );
    }
}