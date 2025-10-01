package com.descodeuses.voyage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.service.UtilisateurService;
import jakarta.servlet.http.HttpSession;


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

       @GetMapping("error")
    public String ShowError(Model model){
        model.addAttribute("ShowError", new Utilisateur());
        return "error";
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
public String login(@ModelAttribute Utilisateur utilisateur, HttpSession session) {
    Utilisateur authenticated = utilisateurService.authenticate(utilisateur.getPseudo(), utilisateur.getMdp());

    if (authenticated == null) {
        // Gérer le cas où l'authentification échoue
        return "redirect:/error";
    }

    // ✅ Stocker l'utilisateur authentifié (avec son rôle) dans la session
    session.setAttribute("authenticatedUser", authenticated);

    // Vérifier si l'utilisateur a le rôle d'administrateur
    if (authenticated.getRole() != null && "ROLE_ADMIN".equals(authenticated.getRole().getNom())) {
        return "redirect:/Admin";
    }

    // Redirection pour les utilisateurs non-admins
    return "redirect:/card";
}


    
    

}
