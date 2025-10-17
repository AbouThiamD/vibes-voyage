package com.descodeuses.voyage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.service.UtilisateurService;
import com.descodeuses.voyage.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;

    // Injection par constructeur (recommandée)
    public UtilisateurController(UtilisateurService utilisateurService,
                                 UtilisateurRepository utilisateurRepository) {
        this.utilisateurService = utilisateurService;
        this.utilisateurRepository = utilisateurRepository;
    }

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

    // Méthode qui utilisait utilisateurRepository (maintenue)
    @GetMapping("/utilisateur/{id}")
    public String voirUtilisateur(@PathVariable Long id, Model model) {
        System.out.println("appel voirUtilisateur id = " + id);
        Utilisateur user = utilisateurRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
             System.out.println("pseudo trouvé = " + user.getPseudo());
        model.addAttribute("pseudo", user.getPseudo());
        return "utilisateur";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Utilisateur utilisateur){
        System.out.println("register request: " + utilisateur);
        Utilisateur registeredUtilisateur = utilisateurService.registerUtilisateur(
            utilisateur.getPseudo(), utilisateur.getMdp(), utilisateur.getEmail());
        return registeredUtilisateur == null ? "error_page" : "redirect:/card";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Utilisateur utilisateur, HttpSession session) {
        Utilisateur authenticated = utilisateurService.authenticate(utilisateur.getPseudo(), utilisateur.getMdp());

        if (authenticated == null) {
            return "redirect:/error";
        }

        session.setAttribute("authenticatedUser", authenticated);

        if (authenticated.getRole() != null && "ROLE_ADMIN".equals(authenticated.getRole().getNom())) {
            return "redirect:/Admin";
        }

        return "redirect:/card";
    }
    


    
}
