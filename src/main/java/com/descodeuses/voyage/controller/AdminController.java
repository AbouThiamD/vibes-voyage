package com.descodeuses.voyage.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller

public class AdminController {
    @Autowired
    private CategorieService categorieService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/Admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/ajouterVideo")
    public String formVideo(Model model) {
        List<Categorie> categories = categorieService.getAllCategorie();
        model.addAttribute("categories",categories);
        return "formVideo";
    }


    

    @GetMapping("/ajouterCategorie")
    public String formCategorie(Model model) {
        model.addAttribute("categories", categorieService.getAllCategorie());
        return "formCategorie";
    }

    @GetMapping("/lesVideos")
    public String allVideo(Model model) {
        model.addAttribute("video",videoService.getAllVideo());
        return "allVideo";
    }

    @GetMapping("/lesUtilisateurs")
    public String allUtilisateur() {
        return "allUtilisateur";
    }

    @GetMapping("/ajouterAdmnistateur")
    public String formAdmin() {
        return "formAdmin";
    }

    @PostMapping("/saveCategorie")
    public String saveCategorie(@ModelAttribute Categorie categorie, HttpSession session) {
        Boolean existCategorie = categorieService.existCategorie(categorie.getNomCategorie());
        if(existCategorie)
        {
            session.setAttribute("errorMsg", "Categorie deja existante");

        }else{
             Categorie saveCategorie = categorieService.saveCategorie(categorie);
             if(ObjectUtils.isEmpty(saveCategorie))
             {
                session.setAttribute("errorMsg", "non sauvegarder veillez bien remplir ");
             }else{
                    session.setAttribute("succMsg", "Catégorie enregistrée avec succès");
             }
        }
        categorieService.saveCategorie(categorie);

        return "redirect:/ajouterCategorie";
    }

    @GetMapping("/deleteCategorie/{id}")
   public String deleteCategorie(@PathVariable long id, HttpSession session) {
    // Appel à la méthode de suppression dans le service
    Boolean deleteCategorie = categorieService.deleteCategorie(id);
    
    // Vérification si la suppression a réussi
    if (deleteCategorie) {
        // Si la suppression a réussi, on met un message de succès dans la session
        session.setAttribute("succMsg", "Categorie supprimée avec succès");
    } else {
        // Si la suppression a échoué, on met un message d'erreur dans la session
        session.setAttribute("errorMsg", "Erreur dans le serveur");
    }
    
    // Redirection vers la page 'ajouterCategorie'
    return "redirect:/ajouterCategorie";
}


    @GetMapping("/loadEditCategorie/{id}")
    public String loadEditCategorie(@PathVariable long id, Model model){
        model.addAttribute("categorie", categorieService.getCategorieById(id));
        return "edit_categorie";
    }

    
    @GetMapping("/deleteVideo/{id}")
    public String loadViewVideo(@PathVariable long id){
      
        return "lesVideos";
    }

    @PostMapping("/updateCategorie")
    public String updateCategorie(@ModelAttribute Categorie categorie, HttpSession session){

        Categorie oldCategorie = categorieService.getCategorieById(categorie.getId());

        if(!ObjectUtils.isEmpty(categorie)) {

            oldCategorie.setNomCategorie(categorie.getNomCategorie());
            oldCategorie.setIsActive(categorie.getIsActive());
          

        }
        Categorie updateCategorie = categorieService.saveCategorie(oldCategorie);
        if(!ObjectUtils.isEmpty(updateCategorie))
        {
            session.setAttribute("succMsg", "Catégorie modifiée avec succès");

        }else{
            session.setAttribute("errorMsg", "Erreur dans le serveur");
        }
        categorieService.saveCategorie(oldCategorie);
        return "redirect:/loadEditCategorie/" +categorie.getId();
    }

    @PostMapping("/saveVideo")
   public String saveVideo(
        @ModelAttribute Video video,
        @RequestParam("file") MultipartFile vid, 
        HttpSession session) throws IOException {
        
        String videoName = vid.isEmpty() ? "default.jpg" : vid.getOriginalFilename();
        video.setVid(videoName);

        Video saveVideo = videoService.saveVideo(video);
        if(!ObjectUtils.isEmpty(saveVideo)){
        File saveFile = new ClassPathResource("static/IMAGES").getFile();

        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "video_img" + File.separator + vid.getOriginalFilename());

        // System.out.printLn(path);
        Files.copy(vid.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        
            session.setAttribute("succMsg","Vidéo sauvegardée avec succès");
        }else{
            session.setAttribute("errorMSG","Probleme du surement au serveur");
        }
        return "redirect:/ajouterVideo";
    }

    

    
}


    