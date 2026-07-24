package com.descodeuses.voyage.controller;

import org.springframework.beans.factory.annotation.Autowired; // <-- NOUVEL IMPORT
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.model.Categorie; 
import com.descodeuses.voyage.repository.CategorieRepository; // 

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {


    @Autowired
    private CategorieRepository categorieRepository;



    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        Object obj = session.getAttribute("authenticatedUser");
        if (obj != null && obj instanceof Utilisateur user) {
            model.addAttribute("currentPseudo", user.getPseudo());
        }
    }

    
    @ModelAttribute("categories")
    public List<Categorie> addCategoriesToModel() {
      
        return categorieRepository.findAll(); 
    }
}