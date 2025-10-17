package com.descodeuses.voyage.controller;

import org.springframework.http.MediaType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.form.VideoForm;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

@Controller
public class VideoUserController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CategorieService categorieService;

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
        // 0) Vérifs rapides
        if (videoFile.isEmpty()) throw new IllegalArgumentException("Fichier vidéo manquant");
        if (categorieId == null) throw new IllegalArgumentException("Catégorie manquante");

        // 1) Dossiers
        Path base = Path.of(storagePath);          // ex: "uploads"
        Path videosDir = base.resolve("videos");
        Path imagesDir = base.resolve("images");
        Files.createDirectories(videosDir);
        Files.createDirectories(imagesDir);

        // 2) Sauvegarde des fichiers
        String videoName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename().replace(" ", "");
        String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename().replace(" ", "");

        Files.copy(videoFile.getInputStream(), videosDir.resolve(videoName), StandardCopyOption.REPLACE_EXISTING);
        if (!imageFile.isEmpty()) {
            Files.copy(imageFile.getInputStream(), imagesDir.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
        }

        // 3) Construire l’entité Video
        Video v = new Video();
        v.setNomVideo(titre);                          // <— correspond à "titre"
        v.setDescription(description);
        v.setDate(new java.sql.Date(System.currentTimeMillis()));
        v.setCategorie(categorieService.getCategorieById(categorieId));
        v.setUrl("/uploads/videos/" + videoName);      // chemin public pour lire la vidéo
        // si tu rajoutes une miniature plus tard : v.setImageUrl("/uploads/images/" + imageName);

        videoService.saveVideo(v);

        ra.addFlashAttribute("succMsg", "🎉 Vidéo envoyée !");
        // 4) Redirection selon la catégorie
        String cat = v.getCategorie().getNomCategorie().toLowerCase();
        switch (cat) {
            case "afrique": return "redirect:/Afrique";
            case "europe": return "redirect:/Europe";
            case "asie": return "redirect:/Asie";
            case "océanie":
            case "oceanie": return "redirect:/Oceanie";
            case "amérique du nord":
            case "amerique du nord": return "redirect:/AmeriqueDuNord";
            case "amérique du sud":
            case "amerique du sud": return "redirect:/AmeriqueDuSud";
            default: return "redirect:/FormulairePage";
        }
    } catch (Exception e) {
        e.printStackTrace();
        ra.addFlashAttribute("errorMsg", "❌ " + e.getMessage());
        return "redirect:/FormulairePage";
    }
}
}
