package com.descodeuses.voyage.controller;

import java.util.List;
import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.CategorieRepository;
import com.descodeuses.voyage.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ContinentController {

    private final VideoRepository videoRepository;
    private final CategorieRepository categorieRepository;

    // /continent?id=32  (recommandé)  ou  /continent?nom=Afrique (fallback)
    @GetMapping("/continent")
    public String continent(@RequestParam(required = false) Long id,
                            @RequestParam(required = false, defaultValue = "Afrique") String nom,
                            Model model) {

        Categorie cat = null;
        List<Video> videos = Collections.emptyList();

        if (id != null) {
            cat = categorieRepository.findById(id).orElse(null);
            videos = videoRepository.findByCategorie_Id(id); // => par ID (fiable)
        }

        if (videos.isEmpty()) { // secours par nom si pas d'ID
            var q = nom.trim();
            cat = (cat != null) ? cat : categorieRepository.findByNomCategorieIgnoreCase(q).orElse(null);
            if (cat != null) videos = videoRepository.findByCategorie_Id(cat.getId());
        }

        model.addAttribute("videos", videos);
        model.addAttribute("cat", cat);
        model.addAttribute("categories", categorieRepository.findAll());
        return "Afrique"; // copie/renomme Afrique.html -> continent.html
    }
}
