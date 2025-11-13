package com.descodeuses.voyage.controller;

import com.descodeuses.voyage.service.EmailService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private EmailService emailService; 

    
    @GetMapping("/contact")
    public String showContactPage() {
        return "Contact"; 
    }

    
    @PostMapping("/contact/send")
    public String handleContactForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            RedirectAttributes ra) {
        
        try {
        
            emailService.sendContactEmail(name, email, message); 
            ra.addFlashAttribute("succMsg", "Votre message a été envoyé avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Erreur : le message n'a pas pu être envoyé.");
        }
        
        return "redirect:/Contact";
    }
}