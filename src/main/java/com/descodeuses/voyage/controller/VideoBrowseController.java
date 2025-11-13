package com.descodeuses.voyage.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.descodeuses.voyage.model.Utilisateur; 
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.VideoRepository;
import com.descodeuses.voyage.repository.CategorieRepository;
import com.descodeuses.voyage.repository.UtilisateurRepository; 

@Controller
@RequiredArgsConstructor
public class VideoBrowseController {

    private final VideoRepository videoRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository; 

    
    @GetMapping("/videos")
    public String listVideos(@RequestParam(required = false) Long categorieId,
                             Model model) {
    
        List<Video> videos;
        if (categorieId != null) {
            videos = videoRepository.findByCategorie_Id(categorieId);
        } else {
            try {
                videos = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
            } catch (Exception ex) {
                videos = videoRepository.findAll();
            }
        }
        model.addAttribute("videos", videos);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("selectedCategorieId", categorieId);
        model.addAttribute("title", "Toutes les vidéos");
        return "allVideoUser"; 
    }

   
    @GetMapping("/toutes-les-videos")
    public String alias(@RequestParam(required = false) Long categorieId) {
        return (categorieId != null)
                ? "redirect:/videos?categorieId=" + categorieId
                : "redirect:/videos";
    }

    
   
    @GetMapping("/dashboard/mes-videos")
    public String mesVideos(@RequestParam(required=false) Long categorieId,
                            Model model, Authentication auth) {
        
        if (auth == null) {
            return "redirect:/login?login";
        }
        
        String pseudo = auth.getName();

       
        Utilisateur userConnecte = utilisateurRepository.findByPseudo(pseudo) 
                .orElse(null); 

        
        if (userConnecte == null) {
            return "redirect:/logout"; 
        }

     
        List<Video> videos;
        if (categorieId == null) {
            // Utilise la nouvelle méthode (on passe l'objet)
            videos = videoRepository.findByUtilisateur(userConnecte);
        } else {
            // Utilise la nouvelle méthode filtrée (on passe l'objet et l'ID)
            videos = videoRepository.findByUtilisateurAndCategorieId(userConnecte, categorieId);
        }

        model.addAttribute("videos", videos);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("selectedCategorieId", categorieId);
        model.addAttribute("title", "Mes vidéos");
        model.addAttribute("owned", true);
        return "allVideoUser";
    }
}