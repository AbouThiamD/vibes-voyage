package com.descodeuses.voyage.controller;

import org.springframework.beans.factory.annotation.Autowired; // <-- NOUVEL IMPORT
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.model.Categorie; // <-- NOUVEL IMPORT
import com.descodeuses.voyage.repository.CategorieRepository; // <-- NOUVEL IMPORT

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Injecter le repository des catégories/continents
    @Autowired
    private CategorieRepository categorieRepository;


    // Méthode 1 : Ajoute le pseudo de l'utilisateur connecté au modèle
    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        Object obj = session.getAttribute("authenticatedUser");
        if (obj != null && obj instanceof Utilisateur user) {
            model.addAttribute("currentPseudo", user.getPseudo());
        }
    }

    // Méthode 2 : Ajoute la liste des catégories/continents au modèle de TOUTES les vues
    @ModelAttribute("categories")
    public List<Categorie> addCategoriesToModel() {
        // Cette méthode s'exécute pour chaque requête et peuple la liste pour le header
        return categorieRepository.findAll(); 
    }
}