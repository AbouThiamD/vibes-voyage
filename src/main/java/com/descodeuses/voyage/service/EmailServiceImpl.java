package com.descodeuses.voyage.service;

import com.descodeuses.voyage.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Lit la variable "app.contact.to" de ton fichier .properties
    @Value("${app.contact.to}")
    private String adminEmailAddress; 

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    @Override
    public void sendContactEmail(String fromName, String fromEmail, String messageBody) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmailAddress);
            
            message.setTo(adminEmailAddress);
            message.setSubject("Vibes Voyage: Nouveau message de " + fromName);
            message.setReplyTo(fromEmail); 

            String fullMessage = "Vous avez reçu un message de : " + fromName + " (" + fromEmail + ")\n\n"
                               + "-----------------------------------\n"
                               + messageBody;
            message.setText(fullMessage);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            // C'est mieux de relancer une exception spécifique si tu veux la gérer plus haut
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}