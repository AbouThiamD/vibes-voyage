package com.descodeuses.voyage.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ✅ à ajouter

import com.descodeuses.voyage.form.VideoForm;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CategorieService categorieService;

    @Value("${video.upload.path}")
    private String storagePath;

    @PostMapping("/private/video/add")
    public String videoSubmit(@ModelAttribute VideoForm videoForm,
                              HttpServletRequest request,
                              RedirectAttributes ra) {
        try {
            Path storageDir = Path.of(storagePath);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            // Sauvegarde du fichier
            String fileName = System.currentTimeMillis() + "_"
                    + videoForm.getFichier().getOriginalFilename().replace(" ", "");
            Path filePath = storageDir.resolve(fileName);
            Files.copy(videoForm.getFichier().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Construire l’objet Video
            Video video = new Video();
            video.setNomVideo(videoForm.getNomVideo());
            video.setUrl(fileName);
            video.setDate(Date.valueOf(LocalDate.now()));
            video.setDescription(videoForm.getDescription());
            video.setCategorie(categorieService.getCategorieById(videoForm.getCategorieId()));

            // Sauvegarde en BDD
            videoService.saveVideo(video);

            // Message de succès
            ra.addFlashAttribute("succMsg", "🎉 Bravo, la vidéo a été téléversée !");
            return "redirect:/ajouterVideo";

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
            return "redirect:/ajouterVideo";
        }
    }
} 