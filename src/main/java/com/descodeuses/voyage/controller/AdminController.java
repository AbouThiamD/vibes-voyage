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
    // null-safe
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
    ra.addFlashAttribute("errorMsg", e.getMessage()); // <- vrai message
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

@GetMapping("/utilisateurs/{id}/delete")
public String deleteUtilisateur(@PathVariable long id, RedirectAttributes ra) {
    try {
        var u = utilisateurService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));
        utilisateurService.supprimer(u);  // ta méthode existante
        ra.addFlashAttribute("succMsg", "Utilisateur supprimé avec succès.");
    } catch (NoSuchElementException e) {
        ra.addFlashAttribute("errorMsg", "Utilisateur introuvable.");
    } catch (DataIntegrityViolationException e) {
        ra.addFlashAttribute("errorMsg", "Impossible de supprimer : utilisateur lié ailleurs.");
    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "Erreur serveur : " + e.getMessage());
    }
    return "redirect:/lesUtilisateurs"; // ta page liste
}


    @PostMapping("/saveCategorie")
    public String saveCategorie(@ModelAttribute Categorie categorie, HttpSession session) {
        Boolean existCategorie = categorieService.existCategorie(categorie.getNomCategorie());
        if (existCategorie) {
            session.setAttribute("errorMsg", "Categorie deja existante");

        } else {
            Categorie saveCategorie = categorieService.saveCategorie(categorie);
            if (ObjectUtils.isEmpty(saveCategorie)) {
                session.setAttribute("errorMsg", "non sauvegarder veillez bien remplir ");
            } else {
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
    public String loadEditCategorie(@PathVariable long id, Model model) {
        model.addAttribute("categorie", categorieService.getCategorieById(id));
        return "edit_categorie";
    }

   @GetMapping("/deleteVideo/{id}")
public String deleteVideo(@PathVariable long id,
                          org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
    try {
        videoService.deleteVideo(id); // <-- appelle ton service
        ra.addFlashAttribute("succMsg", "✅ Vidéo supprimée avec succès !");
    } catch (Exception e) {
        ra.addFlashAttribute("errorMsg", "❌ Erreur lors de la suppression : " + e.getMessage());
    }
    return "redirect:/lesVideos"; // retour vers la liste
}
    // GET: afficher le formulaire rempli
@GetMapping("/videos/{id}/edit")
public String editVideo(@PathVariable Long id, Model model) {
    Video v = videoService.getVideoById(id); // <-- au lieu de findById
    model.addAttribute("video", v);
    model.addAttribute("categories", categorieService.getAllCategorie());
    return "video_edit";
}

@PostMapping("/videos/{id}")
public String updateVideo(@PathVariable Long id, @ModelAttribute("video") Video form,
                          RedirectAttributes ra) {
    Video v = videoService.getVideoById(id); // <-- idem
    // ... mêmes updates que plus haut
    return "redirect:/lesVideos";
}




}

   

