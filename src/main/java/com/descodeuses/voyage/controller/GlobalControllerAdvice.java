package com.descodeuses.voyage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.descodeuses.voyage.model.Utilisateur;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        Object obj = session.getAttribute("authenticatedUser");
        if (obj != null && obj instanceof Utilisateur) {
            Utilisateur user = (Utilisateur) obj;
            model.addAttribute("currentPseudo", user.getPseudo());
        }
    }
}
