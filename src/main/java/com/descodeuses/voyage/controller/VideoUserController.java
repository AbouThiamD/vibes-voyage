package com.descodeuses.voyage.controller;

import org.springframework.http.MediaType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

@Controller
public class VideoUserController {

    @Autowired private VideoService videoService;
    @Autowired private CategorieService categorieService;

    @Value("${video.upload.path}")
    private String storagePath;

@PostMapping(path = "/video/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public String saveVideo(@RequestParam("titre") String titre,
                        @RequestParam(value = "description", required = false) String description,
                        @RequestParam("categorieId") Long categorieId,
                        @RequestParam("videoFile") MultipartFile videoFile,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        RedirectAttributes ra) {
    try {
        // 0) Vérifs
        if (videoFile.isEmpty()) throw new IllegalArgumentException("Fichier vidéo manquant");
        if (categorieId == null) throw new IllegalArgumentException("Catégorie manquante");

        // 1) Dossiers
        Path base = Path.of(storagePath);     // ex: upload/video
        Path videosDir = base.resolve("videos");
        Path imagesDir = base.resolve("images");
        Files.createDirectories(videosDir);
        Files.createDirectories(imagesDir);

        // 2) Sauvegarde de la VIDÉO
        String safeVideoName = System.currentTimeMillis() + "_" +
                videoFile.getOriginalFilename().replaceAll("\\s+", "");
        Files.copy(videoFile.getInputStream(), videosDir.resolve(safeVideoName),
                StandardCopyOption.REPLACE_EXISTING);
        String videoUrl = "/uploads/videos/" + safeVideoName; // URL publique

        // 3) Sauvegarde de la MINIATURE (optionnelle) + URL publique
        String imageUrl;
        if (!imageFile.isEmpty()) {
         String safeImageName = System.currentTimeMillis() + "_" +
            imageFile.getOriginalFilename().replaceAll("\\s+", "");
         Files.copy(imageFile.getInputStream(), imagesDir.resolve(safeImageName),
            StandardCopyOption.REPLACE_EXISTING);
         imageUrl = "/uploads/images/" + safeImageName;
        } else {
      imageUrl = "/IMAGES/Continents/afrique-hero.jpg"; // fallback
}

        Video v = new Video();
        v.setNomVideo(titre);
        v.setDescription(description);
        v.setDate(new java.sql.Date(System.currentTimeMillis()));
        v.setCategorie(categorieService.getCategorieById(categorieId));
        v.setUrl("/uploads/videos/" + safeVideoName);
        v.setImage(imageUrl); // ✅ ICI, pas ailleurs
        videoService.saveVideo(v);

        ra.addFlashAttribute("succMsg", "🎉 Vidéo envoyée !");
        return "redirect:/continents?id=" + v.getCategorie().getId() + "#videos";

    } catch (Exception e) {
        e.printStackTrace();
        ra.addFlashAttribute("errorMsg", "❌ " + e.getMessage());
        return "redirect:/FormulairePage";
    }
}
}