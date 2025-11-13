package com.descodeuses.voyage.controller;

import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.service.UtilisateurService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/compte")
public class CompteController {

    private final UtilisateurService utilisateurService;

    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable long id,
                            Authentication auth,
                            HttpServletRequest req) {

        Utilisateur me = utilisateurService.findByPseudo(auth.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        boolean isSelf  = me.getId().equals(id);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isSelf && !isAdmin) throw new AccessDeniedException("Non autorisé");

        Utilisateur cible = utilisateurService.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));
        utilisateurService.supprimer(cible);

        var s = req.getSession(false); if (s != null) s.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/?deleted=1";
    }
}
