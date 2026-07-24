package com.descodeuses.voyage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.service.UtilisateurService;
import com.descodeuses.voyage.repository.UtilisateurRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.model.Role;
import com.descodeuses.voyage.repository.RoleRepository;
import com.descodeuses.voyage.service.RoleService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final RoleService roleService; 
    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    
    public UtilisateurController(UtilisateurService utilisateurService,
                                 UtilisateurRepository utilisateurRepository,
                                 RoleRepository roleRepository,
                                 RoleService roleService,
                                 PasswordEncoder passwordEncoder) { 
        
        this.utilisateurService = utilisateurService;
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        
       
        this.roleService = roleService; 
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new Utilisateur());
        return "register_page";
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

    
    @GetMapping("/utilisateur/{id}")
    public String voirUtilisateur(@PathVariable Long id, Model model) {
       
        Utilisateur user = utilisateurRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
          
        model.addAttribute("pseudo", user.getPseudo());
        return "utilisateur";
    }

@PostMapping("/register")
public String register(@ModelAttribute Utilisateur utilisateur,
                       RedirectAttributes ra,
                       HttpServletRequest request) { 
    try {
       
        Role userRole = roleRepository.findByNom("USER")
                .orElseThrow(() -> new IllegalStateException("Le Rôle 'USER' est manquant en base de données."));
        
       
        utilisateur.setRole(userRole);
        String motDePasseNonHache = utilisateur.getMdp();
        utilisateur.setMdp(passwordEncoder.encode(motDePasseNonHache)); 
        
      
        utilisateurRepository.save(utilisateur);
      
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            utilisateur.getPseudo(), 
            null, 
        
            Collections.singletonList(new SimpleGrantedAuthority(userRole.getNom())) 
        );
        
       
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authToken);
        
       
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        
       
        session.setAttribute("authenticatedUser", utilisateur); 

      
        return "redirect:/card"; 
        
    } catch (IllegalStateException | IllegalArgumentException e) {
       
        ra.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:/register"; 
    }
}

@PostMapping("/login")
public String login(@ModelAttribute Utilisateur utilisateur,
                    HttpSession session,
                    org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {

    var authenticated = utilisateurService.authenticate(utilisateur.getPseudo(), utilisateur.getMdp());
    if (authenticated == null) {
        ra.addFlashAttribute("errorTitle", "Connexion impossible");
        ra.addFlashAttribute("errorMsg", "Pseudo ou mot de passe incorrect.");
        ra.addFlashAttribute("prefillPseudo", utilisateur.getPseudo()); 
        return "redirect:/error"; 
    }

    session.setAttribute("authenticatedUser", authenticated);
    if (authenticated.getRole() != null && "ROLE_ADMIN".equals(authenticated.getRole().getNom())) {
        return "redirect:/Admin";
    }
    return "redirect:/card";
}

    


    
}
