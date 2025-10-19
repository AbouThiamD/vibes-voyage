package com.descodeuses.voyage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.service.UtilisateurService;

@Controller
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder; // déjà déclaré dans ta config security

    public ProfilController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
    }

   @GetMapping("/profil")
    public String profil(Model model, Authentication auth, RedirectAttributes ra) {
        if (auth == null) {
            ra.addFlashAttribute("errorMsg", "Vous devez être connecté.");
            return "redirect:/login";
        }
        var u = utilisateurService.findByPseudo(auth.getName())
                 .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
        model.addAttribute("u", u);
        return "profil"; // templates/profil.html
    }

    // Mise à jour email/pseudo + changement de mot de passe (optionnel)
    @PostMapping("/profil")
    public String updateProfil(@RequestParam String email,
                               @RequestParam String pseudo,
                               @RequestParam(required=false) String currentPassword,
                               @RequestParam(required=false) String newPassword,
                               @RequestParam(required=false) String confirmPassword,
                               Authentication auth,
                               RedirectAttributes ra) {
        if (auth == null) {
            ra.addFlashAttribute("errorMsg", "Vous devez être connecté.");
            return "redirect:/login";
        }

        var u = utilisateurService.findByPseudo(auth.getName())
                 .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));

        // MAJ champs simples
        u.setEmail(email.trim());
        u.setPseudo(pseudo.trim());

        // Changement de mot de passe (si renseigné)
        if (newPassword != null && !newPassword.isBlank()) {
            if (currentPassword == null || !passwordEncoder.matches(currentPassword, u.getMdp())) {
                ra.addFlashAttribute("errorMsg", "Mot de passe actuel incorrect.");
                return "redirect:/profil";
            }
            if (!newPassword.equals(confirmPassword)) {
                ra.addFlashAttribute("errorMsg", "Le nouveau mot de passe et la confirmation ne correspondent pas.");
                return "redirect:/profil";
            }
            u.setMdp(passwordEncoder.encode(newPassword));
        }

        utilisateurService.save(u); // ou modifier(u)
        ra.addFlashAttribute("succMsg", "Profil mis à jour ✔");
        return "redirect:/profil";
    }
    @PostMapping("/profil/password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        var u = utilisateurService.findByPseudo(auth.getName()).orElseThrow();

        if (!passwordEncoder.matches(oldPassword, u.getPasswordHash())) {
            ra.addFlashAttribute("errorMsg", "Ancien mot de passe incorrect.");
            return "redirect:/profil";
        }
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("errorMsg", "Les mots de passe ne correspondent pas.");
            return "redirect:/profil";
        }
        if (newPassword.length() < 8) {
            ra.addFlashAttribute("errorMsg", "Le nouveau mot de passe doit contenir au moins 8 caractères.");
            return "redirect:/profil";
        }

        u.mdp(passwordEncoder.encode(newPassword));
        utilisateurService.save(u);

        ra.addFlashAttribute("succMsg", "Mot de passe changé.");
        return "redirect:/profil";
    }
}
