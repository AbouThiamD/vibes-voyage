package com.descodeuses.voyage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;

    }

    public Utilisateur registerUtilisateur(String pseudo, String mdp, String email) {
        if (pseudo == null || mdp == null || email == null) {
            return null; 
        } else {
            if (utilisateurRepository.findFirstByPseudo(pseudo).isPresent()) {
                System.out.println("pseudo deja utilisé");
                return null;
            }
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(pseudo);
            utilisateur.setMdp(mdp);
            utilisateur.setEmail(email);
            return utilisateurRepository.save(utilisateur); // Enregistre l'utilisateur dans la base de données
        }
    }

    public Utilisateur authenticate(String pseudo, String mdp) {
        return utilisateurRepository.findByPseudoAndMdp(pseudo, mdp).orElse(null);
    }

    // crud

    public List<Utilisateur> list() {
        return utilisateurRepository.findAll();
    }
    // il ya utilisateur () pour savoir on supprime quoi
    public void supprimer(Utilisateur utilisateur){

        utilisateurRepository.delete(utilisateur);
    }

    public void modifier(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }
    
}
