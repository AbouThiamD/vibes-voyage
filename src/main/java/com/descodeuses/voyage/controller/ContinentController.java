package com.descodeuses.voyage.controller;

import java.util.List;
import java.util.Collections;
import java.util.Locale;

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

    
    @GetMapping("/continent")
    public String redirectContinentSingulier(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nom
    ) {
        if (id != null) {
            return "redirect:/continents?id=" + id;
        }
        if (nom != null && !nom.isBlank()) {
            String q = java.net.URLEncoder.encode(nom, java.nio.charset.StandardCharsets.UTF_8);
            return "redirect:/continents?nom=" + q;
        }
        return "redirect:/continents";
    }

    @GetMapping("/continents")
    public String continent(@RequestParam(required = false) Long id,
                            @RequestParam(required = false, defaultValue = "Afrique") String nom,
                            Model model) {

        Categorie cat = null;
        List<Video> videos = Collections.emptyList();

        // 1) Priorité: par ID
        if (id != null) {
            cat = categorieRepository.findById(id).orElse(null);
            if (cat != null) {
                videos = videoRepository.findByCategorie_Id(id);
            }
        }

        // 2) Secours: par nom
        if (videos.isEmpty()) {
            String q = nom == null ? "" : nom.trim();
            if (cat == null) {
                cat = categorieRepository.findByNomCategorieIgnoreCase(q).orElse(null);
            }
            if (cat != null) {
                videos = videoRepository.findByCategorie_Id(cat.getId());
            }
        }

        // 3) Image héro & slug
        String label = (cat != null) ? cat.getNomCategorie() : nom;
        String key = (label == null ? "default"
                                    : label.replace('\u00A0',' ').trim().toLowerCase(Locale.ROOT));

        // ⚠️ respecte la casse de TON dossier d’images
        final String HERO_BASE = "/IMAGES/Continents/";

        String heroImage;
        String slug;

        switch (key) {
            case "afrique" -> { heroImage = HERO_BASE + "afrique-hero.jpg"; slug = "afrique"; }
            case "asie"    -> { heroImage = HERO_BASE + "asie-hero.jpg";    slug = "asie"; }
            case "europe"  -> { heroImage = HERO_BASE + "europe-hero.jpg";  slug = "europe"; }

            // Amériques — adapte aux noms de fichiers que TU as réellement
            case "amérique du nord", "amerique du nord", "amérique nord", "amerique nord",
                 "amerique-nord", "amérique-nord", "ameriquenord" -> {
                heroImage = HERO_BASE + "ameriquenord-hero.jpg";      
                slug = "ameriquenord";
            }
            case "amérique du sud", "amerique du sud", "amérique sud", "amerique sud",
                 "amerique-sud", "amérique-sud", "ameriquesud" -> {
                heroImage = HERO_BASE + "ameriquesud-hero.jpg";
                slug = "ameriquesud";
            }

            case "océanie", "oceanie" -> { heroImage = HERO_BASE + "oceanie-hero.jpg"; slug = "oceanie"; }
            case "antarctique"        -> { heroImage = HERO_BASE + "antarctique-hero.jpg"; slug = "antarctique"; }
            default -> { heroImage = HERO_BASE + "default-hero.jpg"; slug = "default"; }
        }

        // 4) Modèle
        model.addAttribute("videos", videos);
        model.addAttribute("cat", cat);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("title", label);
        model.addAttribute("slug", slug);
        model.addAttribute("heroImage", heroImage);
        model.addAttribute("heroAlt", "Paysages de " + label);
        model.addAttribute("activeContinent", slug);

        // ✅ retourne EXACTEMENT "Continents" (même casse que ton fichier)
        return "Continents";
    }
}
