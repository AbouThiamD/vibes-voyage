package com.descodeuses.voyage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.VideoRepository;

@Controller
@RequiredArgsConstructor
public class VideoPageController {

    private final VideoRepository videoRepository;

    @GetMapping("/watch/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Video v = videoRepository.findById(id).orElse(null);
        if (v == null) {
            ra.addFlashAttribute("errorMsg", "Vidéo introuvable.");
            return "redirect:/card"; 
        }
        model.addAttribute("v", v);
        return "VideoDetail";
    }
}
