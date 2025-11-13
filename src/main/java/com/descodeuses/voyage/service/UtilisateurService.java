package com.descodeuses.voyage.service;

import java.util.List;
import java.util.Optional;
import com.descodeuses.voyage.model.Utilisateur;

public interface UtilisateurService {
  Utilisateur registerUtilisateur(String pseudo, String mdp, String email);

  Utilisateur authenticate(String pseudo, String mdp);
  
  Utilisateur creerAdmin(String pseudo, String mdp, String email);

 Utilisateur save(Utilisateur utilisateur);           

Optional<Utilisateur> findByPseudo(String pseudo);

List<Utilisateur> list();
  
  Optional<Utilisateur> findById(long id);
  void supprimer(Utilisateur utilisateur);

  void modifier(Utilisateur utilisateur);
}
