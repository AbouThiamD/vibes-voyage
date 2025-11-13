package com.descodeuses.voyage.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.descodeuses.voyage.service.UtilisateurService; 
import com.descodeuses.voyage.form.AdminForm; 
import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.CategorieRepository;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;


import jakarta.servlet.http.HttpSession;

@Controller

public class AdminController {
    @Autowired
    private CategorieService categorieService;

    @Autowired
    private VideoService videoService;


    @Autowired 
    private UtilisateurService utilisateurService;

    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping("/Admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/ajouterVideo")
    public String ajouterVideo(Model model) {
        List<Categorie> categories = categorieService.getAllCategorie();
        model.addAttribute("categories", categories);
        return "formVideo";
    }

    @GetMapping("/ajouterCategorie")
    public String formCategorie(Model model) {
        model.addAttribute("categories", categorieService.getAllCategorie());
        return "formCategorie";
    }

    @GetMapping("/lesVideos")
    public String allVideo(Model model) {
        model.addAttribute("video", videoService.getAllVideo());
        return "allVideo";
    }
    @PostMapping("/utilisateurs")
    public String ajouterAdministrateur(@ModelAttribute("form") AdminForm form,
                                    org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
    
    String login = form.getLogin() == null ? "" : form.getLogin().trim();
    String email = form.getEmail() == null ? "" : form.getEmail().trim();
    String motDePasse = form.getMotDePasse() == null ? "" : form.getMotDePasse().trim();

    if (login.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
        ra.addFlashAttribute("errorMsg", "Veuillez remplir tous les champs.");
        ra.addFlashAttribute("form", form);
        return "redirect:/ajouterAdministrateur";
    }

    try {
        utilisateurService.creerAdmin(login, motDePasse, email);
        ra.addFlashAttribute("succMsg", "Nouvel administrateur ajouté avec succès.");
        return "redirect:/ajouterAdministrateur";
    } catch (Exception e) {
    ra.addFlashAttribute("errorMsg", e.getMessage());
    ra.addFlashAttribute("form", form);
    return "redirect:/ajouterAdministrateur";
}

}

@GetMapping("/ajouterAdministrateur")
public String formAdmin(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new AdminForm());
    }
    return "formAdmin";
}


   @GetMapping("/lesUtilisateurs")
public String allUtilisateur(Model model) {
    model.addAttribute("utilisateurs", utilisateurService.list()); // <-- ajoute la liste
    return "allUtilisateur";
}

@PostMapping("/admin/utilisateurs/{id}/delete")
public String deleteUtilisateur(@PathVariable long id, RedirectAttributes ra) {
    try {
        var u = utilisateurService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));
        utilisateurService.supprimer(u);
        ra.addFlashAttribute("succMsg", "Utilisateur supprimé avec succès.");
    } catch (NoSuchElementException e) {
        ra.addFlashAttribute("errorMsg", "Utilisateur introuvable.");
    } catch (DataIntegrityViolationException e) {
        ra.addFlashAttribute("errorMsg", "Impossible de supprimer : utilisateur lié ailleurs.");
    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "Erreur serveur : " + e.getMessage());
    }
    return "redirect:/lesUtilisateurs";
}


    @PostMapping("/saveCategorie")
    public String saveCategorie(@ModelAttribute Categorie categorie, RedirectAttributes ra) {
    Boolean existCategorie = categorieService.existCategorie(categorie.getNomCategorie());
    if (existCategorie) {
        ra.addFlashAttribute("errorMsg", "Categorie deja existante");
    } else {
        Categorie saveCategorie = categorieService.saveCategorie(categorie);
        if (ObjectUtils.isEmpty(saveCategorie)) {
            ra.addFlashAttribute("errorMsg", "non sauvegarder veillez bien remplir ");
        } else {
            ra.addFlashAttribute("succMsg", "Catégorie enregistrée avec succès");
        }
    }
    
    return "redirect:/ajouterCategorie";
}

  @PostMapping("/admin/categories/{id}/delete")
public String deleteCategorie(@PathVariable long id, RedirectAttributes ra) {
    try {
        boolean ok = categorieService.deleteCategorie(id);
        ra.addFlashAttribute(ok ? "succMsg" : "errorMsg",
            ok ? "Catégorie supprimée." : "Suppression impossible.");
    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
    }
    return "redirect:/ajouterCategorie";
}

    @GetMapping("/loadEditCategorie/{id}")
    public String loadEditCategorie(@PathVariable long id, Model model) {
        model.addAttribute("categorie", categorieService.getCategorieById(id));
        return "edit_categorie";
    }

    @PostMapping("/admin/videos/{id}/delete")
    public String deleteVideo(@PathVariable long id, RedirectAttributes ra) {
    try {
        videoService.deleteVideo(id);
        ra.addFlashAttribute("succMsg", "Vidéo supprimée.");
    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
    }
    return "redirect:/lesVideos";
}

   
 @GetMapping("/videos/{id}/edit") 
public String editVideo(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
    
    try {
        
        Video video = videoService.getVideoById(id); 
        
        
        model.addAttribute("video", video);
        model.addAttribute("categories", categorieRepository.findAll()); 
        return "video_edit"; 

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute("errorMsg", "Erreur : La vidéo avec l'ID " + id + " n'existe pas.");
        return "redirect:/Admin"; 
    }
}

@PostMapping("/videos/{id}")
public String updateVideo(@PathVariable Long id, 
                            @ModelAttribute("video") Video videoModifiee,
                            @RequestParam("categorieId") Long categorieId,
                            RedirectAttributes ra) {
    try {
        
        Video v = videoService.getVideoById(id); 
        
        
        Categorie cat = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

      
        v.setNomVideo(videoModifiee.getNomVideo()); 
        v.setDescription(videoModifiee.getDescription());
        v.setCategorie(cat); 

     
        videoService.saveVideo(v); 
        
        ra.addFlashAttribute("succMsg", "Vidéo mise à jour.");
        return "redirect:/lesVideos";

    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "Erreur lors de la mise à jour: " + e.getMessage());
        return "redirect:/videos/" + id + "/edit"; 
    }



}
}

   

