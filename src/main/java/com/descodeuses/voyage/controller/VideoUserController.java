package com.descodeuses.voyage.controller;

import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

// ↓ à adapter si tu utilises un Repository au lieu d'un Service
import com.descodeuses.voyage.service.UtilisateurService;

@Controller
public class VideoUserController {

    @Autowired private VideoService videoService;
    @Autowired private CategorieService categorieService;
    @Autowired private UtilisateurService utilisateurService; // pour trouver l'utilisateur courant

    @Value("${video.upload.path}")
    private String storagePath;

    // ---------------------------
    // UPLOAD d'une vidéo (UTILISATEUR)
    // ---------------------------
    @PostMapping(path = "/video/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveVideo(@RequestParam("titre") String titre,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam("categorieId") Long categorieId,
                            @RequestParam("videoFile") MultipartFile videoFile,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            Authentication auth,
                            RedirectAttributes ra) {
        try {
            // 0) Vérifs
            if (videoFile.isEmpty()) throw new IllegalArgumentException("Fichier vidéo manquant");
            if (categorieId == null) throw new IllegalArgumentException("Catégorie manquante");

            // 1) Dossiers
            Path base = Path.of(storagePath);     // ex: C:/.../upload/   (racine de /uploads/)
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

            // 4) Persist
            Video v = new Video();
            v.setNomVideo(titre);
            v.setDescription(description);
            v.setDate(new java.sql.Date(System.currentTimeMillis()));
            v.setCategorie(categorieService.getCategorieById(categorieId));
            v.setUrl(videoUrl);
            v.setImage(imageUrl);

            // 🔑 Définir le propriétaire (owner) de la vidéo
            if (auth != null) {
                var user = utilisateurService.findByPseudo(auth.getName())
                        .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
                v.setUtilisateur(user);
            }

            videoService.saveVideo(v);

            ra.addFlashAttribute("succMsg", "🎉 Vidéo envoyée !");
            return "redirect:/continents?id=" + v.getCategorie().getId() + "#videos";

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMsg", "❌ " + e.getMessage());
            return "redirect:/FormulairePage";
        }
    }

         @PostMapping("/video/{id}/delete")
        public String deleteOwn(@PathVariable Long id,
                        Authentication auth,
                        RedirectAttributes ra) {

    Video v = videoService.getById(id);
    String current = (auth != null) ? auth.getName() : null;
    String owner   = (v.getUtilisateur() != null) ? v.getUtilisateur().getPseudo() : null;

    // PATCH: attribue l’owner si manquant (anciennes vidéos)
    if (owner == null && current != null) {
        var user = utilisateurService.findByPseudo(current)
                     .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
        v.setUtilisateur(user);
        videoService.saveVideo(v);
        owner = user.getPseudo();
    }

    if (current == null || owner == null || !current.equalsIgnoreCase(owner)) {
        ra.addFlashAttribute("errorMsg", "Action refusée.");
        return "redirect:/videos";
    }

    boolean ok = videoService.deleteVideo(id);
    boolean stillThere = videoService.existsById(id);

    ra.addFlashAttribute(ok && !stillThere ? "succMsg" : "errorMsg",
            ok && !stillThere ? "✅ Vidéo supprimée." : "❌ Suppression non effectuée.");
    return "redirect:/videos"; // ou /mes-videos si tu l’as créée
}


  @GetMapping("/video/{id}/edit")
public String editForm(@PathVariable Long id,
                       Authentication auth,
                       Model model,
                       RedirectAttributes ra) {
    // 1) Charger la vidéo
    Video v = videoService.getById(id);

    // 2) Récupérer l'utilisateur courant (en base)
    if (auth == null) {
        ra.addFlashAttribute("errorMsg", "Action refusée (non connecté).");
        return "redirect:/videos";
    }

    var currentOpt = utilisateurService.findByPseudo(auth.getName()); // adapte si ton login n’est pas le pseudo
    if (currentOpt.isEmpty()) {
        ra.addFlashAttribute("errorMsg", "Action refusée (utilisateur inconnu).");
        return "redirect:/videos";
    }
    var currentUser = currentOpt.get();

    // 3) PATCH : si la vidéo n'a pas de propriétaire (anciennes vidéos), on l'assigne au user courant
    if (v.getUtilisateur() == null) {
        v.setUtilisateur(currentUser);
        videoService.saveVideo(v);
    }

    // 4) Vérif propriétaire par ID
    boolean canEdit = (v.getUtilisateur() != null)
            && v.getUtilisateur().getId().equals(currentUser.getId());
    // (optionnel) autoriser admin :
    // canEdit = canEdit || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    if (!canEdit) {
        ra.addFlashAttribute("errorMsg", "Action refusée.");
        return "redirect:/videos";
    }

    // 5) OK → préparer la vue
    model.addAttribute("v", v);
    model.addAttribute("id", id);
    model.addAttribute("categories", categorieService.getAllCategorie());
    return "VideoEdit";
}




    @PostMapping(path = "/video/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String editSubmit(@PathVariable Long id,
                         @RequestParam("titre") String titre,
                         @RequestParam(value = "description", required = false) String description,
                         @RequestParam("categorieId") Long categorieId,
                         @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Authentication auth,
                         RedirectAttributes ra) {
    try {
        Video v = videoService.getById(id);

        String current = (auth != null) ? auth.getName() : null;
        String owner   = (v.getUtilisateur()!=null) ? v.getUtilisateur().getPseudo() : null;
        if (current == null || owner == null || !current.equalsIgnoreCase(owner)) {
            ra.addFlashAttribute("errorMsg", "Action refusée.");
            return "redirect:/videos";
        }

        // Mettre à jour les champs simples
        v.setNomVideo(titre);
        v.setDescription(description);
        v.setCategorie(categorieService.getCategorieById(categorieId));

        // Répertoires
        Path base = Path.of(storagePath);   // ex: C:/.../upload/ (racine de /uploads/)
        Path videosDir = base.resolve("videos");
        Path imagesDir = base.resolve("images");
        Files.createDirectories(videosDir);
        Files.createDirectories(imagesDir);

        // Remplacement éventuel de la VIDÉO
        if (videoFile != null && !videoFile.isEmpty()) {
            // supprimer l’ancienne si elle existe
            try {
                if (v.getUrl() != null && !v.getUrl().isBlank()) {
                    java.nio.file.Files.deleteIfExists(videoService.getVideoPath(v.getUrl()));
                }
            } catch (Exception ignored) {}

            String safeVideoName = System.currentTimeMillis() + "_" +
                    videoFile.getOriginalFilename().replaceAll("\\s+", "");
            Files.copy(videoFile.getInputStream(), videosDir.resolve(safeVideoName),
                    StandardCopyOption.REPLACE_EXISTING);
            v.setUrl("/uploads/videos/" + safeVideoName);
        }

        // Remplacement éventuel de l’IMAGE
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (v.getImage() != null && !v.getImage().isBlank()) {
                    java.nio.file.Files.deleteIfExists(videoService.getVideoPath(v.getImage()));
                }
            } catch (Exception ignored) {}

            String safeImageName = System.currentTimeMillis() + "_" +
                    imageFile.getOriginalFilename().replaceAll("\\s+", "");
            Files.copy(imageFile.getInputStream(), imagesDir.resolve(safeImageName),
                    StandardCopyOption.REPLACE_EXISTING);
            v.setImage("/uploads/images/" + safeImageName);
        }

        videoService.saveVideo(v);
        ra.addFlashAttribute("succMsg", "✅ Vidéo mise à jour.");
        return "redirect:/videos"; // ou "redirect:/video/" + id pour revenir à la page détail

    } catch (Exception e) {
        e.printStackTrace();
        ra.addFlashAttribute("errorMsg", "❌ " + e.getMessage());
        return "redirect:/videos";
    }
}


}
