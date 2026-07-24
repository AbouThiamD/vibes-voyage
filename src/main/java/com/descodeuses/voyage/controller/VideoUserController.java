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


import com.descodeuses.voyage.service.UtilisateurService;

@Controller
public class VideoUserController {

    @Autowired private VideoService videoService;
    @Autowired private CategorieService categorieService;
    @Autowired private UtilisateurService utilisateurService; 

    @Value("${video.upload.path}")
    private String storagePath;

    
    @PostMapping(path = "/video/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveVideo(@RequestParam("titre") String titre,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam("categorieId") Long categorieId,
                            @RequestParam("videoFile") MultipartFile videoFile,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            Authentication auth,
                            RedirectAttributes ra) {
        try {
            
            if (videoFile.isEmpty()) throw new IllegalArgumentException("Fichier vidéo manquant");
            if (categorieId == null) throw new IllegalArgumentException("Catégorie manquante");

            
            Path base = Path.of(storagePath);      
            Path videosDir = base.resolve("videos"); 
            Path imagesDir = base.resolve("images"); 
            Files.createDirectories(videosDir);
            Files.createDirectories(imagesDir);

            
            String safeVideoName = System.currentTimeMillis() + "_" +
                    videoFile.getOriginalFilename().replaceAll("\\s+", "");
            
            
            String relativeVideoPath = "videos/" + safeVideoName;

            Files.copy(videoFile.getInputStream(), base.resolve(relativeVideoPath), 
                    StandardCopyOption.REPLACE_EXISTING);
            
            

            
            String relativeImagePath; 
            if (!imageFile.isEmpty()) {
                String safeImageName = System.currentTimeMillis() + "_" +
                        imageFile.getOriginalFilename().replaceAll("\\s+", "");
                
                
                relativeImagePath = "images/" + safeImageName; 

                Files.copy(imageFile.getInputStream(), base.resolve(relativeImagePath),
                        StandardCopyOption.REPLACE_EXISTING);
                
                
            } else {
                relativeImagePath = "/IMAGES/Continents/afrique-hero.jpg"; 
            }

            
            Video v = new Video();
            v.setNomVideo(titre);
            v.setDescription(description);
            v.setDate(new java.sql.Date(System.currentTimeMillis()));
            v.setCategorie(categorieService.getCategorieById(categorieId));
            
            
            v.setUrl(relativeVideoPath); 
            
            v.setImage(relativeImagePath); 

            
            if (auth != null) {
                var user = utilisateurService.findByPseudo(auth.getName())
                        .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
                v.setUtilisateur(user);
            }

            videoService.saveVideo(v);

            ra.addFlashAttribute("succMsg", " Vidéo envoyée !");
            return "redirect:/continents?id=" + v.getCategorie().getId() + "#videos";

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMsg", "Une erreur est survenue : " + e.getMessage());
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
        return "redirect:/videos"; 
    }


    @GetMapping("/video/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Authentication auth,
                           Model model,
                           RedirectAttributes ra) {
        
        Video v = videoService.getById(id);

        
        if (auth == null) {
            ra.addFlashAttribute("errorMsg", "Action refusée (non connecté).");
            return "redirect:/videos";
        }

        var currentOpt = utilisateurService.findByPseudo(auth.getName());
        if (currentOpt.isEmpty()) {
            ra.addFlashAttribute("errorMsg", "Action refusée (utilisateur inconnu).");
            return "redirect:/videos";
        }
        var currentUser = currentOpt.get();

       
        if (v.getUtilisateur() == null) {
            v.setUtilisateur(currentUser);
            videoService.saveVideo(v);
        }

       
        boolean canEdit = (v.getUtilisateur() != null)
                && v.getUtilisateur().getId().equals(currentUser.getId());


        if (!canEdit) {
            ra.addFlashAttribute("errorMsg", "Action refusée.");
            return "redirect:/videos";
        }

    
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

           
            v.setNomVideo(titre);
            v.setDescription(description);
            v.setCategorie(categorieService.getCategorieById(categorieId));

           
            Path base = Path.of(storagePath); 
            Path videosDir = base.resolve("videos");
            Path imagesDir = base.resolve("images");
            Files.createDirectories(videosDir);
            Files.createDirectories(imagesDir);

          
            if (videoFile != null && !videoFile.isEmpty()) {
              
                try {
                    if (v.getUrl() != null && !v.getUrl().isBlank()) {
                        java.nio.file.Files.deleteIfExists(videoService.getVideoPath(v.getUrl()));
                    }
                } catch (Exception ignored) {}

                String safeVideoName = System.currentTimeMillis() + "_" +
                        videoFile.getOriginalFilename().replaceAll("\\s+", "");
                
              
                String relativeVideoPath = "videos/" + safeVideoName;
                
                Files.copy(videoFile.getInputStream(), base.resolve(relativeVideoPath),
                        StandardCopyOption.REPLACE_EXISTING);
                
               
                v.setUrl(relativeVideoPath);
            }

            
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    if (v.getImage() != null && !v.getImage().isBlank()) {
                        java.nio.file.Files.deleteIfExists(videoService.getVideoPath(v.getImage()));
                    }
                } catch (Exception ignored) {}

                String safeImageName = System.currentTimeMillis() + "_" +
                        imageFile.getOriginalFilename().replaceAll("\\s+", "");
                
              
                String relativeImagePath = "images/" + safeImageName;

                Files.copy(imageFile.getInputStream(), base.resolve(relativeImagePath),
                        StandardCopyOption.REPLACE_EXISTING);
                
               
                v.setImage(relativeImagePath);
            }

            videoService.saveVideo(v);
            ra.addFlashAttribute("succMsg", "✅ Vidéo mise à jour.");
            return "redirect:/videos"; 

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMsg", "❌ " + e.getMessage());
            return "redirect:/videos";
        }
    }
}