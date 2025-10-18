package com.descodeuses.voyage.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.VideoRepository;
import com.descodeuses.voyage.repository.CategorieRepository;

@Controller
@RequiredArgsConstructor
public class VideoBrowseController {

    private final VideoRepository videoRepository;
    private final CategorieRepository categorieRepository;

    @GetMapping("/videos")
    public String listVideos(@RequestParam(required = false) Long categorieId,
                             Model model) {

        List<Video> videos;
        if (categorieId != null) {
            videos = videoRepository.findByCategorie_Id(categorieId);
        } else {
            // Si ton repo étend JpaRepository, tri par date (desc)
            try {
                videos = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
            } catch (Exception ex) {
                // Fallback si findAll(Sort) n’existe pas
                videos = videoRepository.findAll();
            }
        }

        model.addAttribute("videos", videos);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("selectedCategorieId", categorieId);
        model.addAttribute("title", "Toutes les vidéos");
        return "allVideoUser"; // => templates/allVideoUser.html
    }

    // Alias pratique
    @GetMapping("/toutes-les-videos")
    public String alias(@RequestParam(required = false) Long categorieId) {
        return (categorieId != null)
                ? "redirect:/videos?categorieId=" + categorieId
                : "redirect:/videos";
    }
}
