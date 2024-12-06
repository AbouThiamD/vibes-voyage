package com.descodeuses.voyage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.service.UtilisateurService;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new Utilisateur());
        return "register_page";
    }

    @GetMapping("login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new Utilisateur());
        return "login_page";
    }

    @GetMapping("/")
    public String getHome(){
        return "register1";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Utilisateur utilisateur){
        System.out.println("register request: " + utilisateur);
     Utilisateur registeredUtilisateur = utilisateurService.registerUtilisateur(utilisateur.getPseudo(), utilisateur.getMdp(), utilisateur.getEmail());
        return registeredUtilisateur == null ? "error_page" : "redirect:/card";
    }

      


    @PostMapping("/login")
    public String login(@ModelAttribute Utilisateur utilisateur, Model model) {
        System.out.println("login request: " + utilisateur);
        Utilisateur authenticated = utilisateurService.authenticate(utilisateur.getPseudo(), utilisateur.getMdp());
        // Vérification des champs vides
        if (authenticated != null) {
            model.addAttribute("utilisateurPseudo",authenticated.getPseudo());
            return "/card"; 
        }else{
            return "error_page";
        }
    
       
    }

    
    

}
