package com.descodeuses.voyage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.descodeuses.voyage.model.Role;
import com.descodeuses.voyage.repository.RoleRepository;
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.repository.UtilisateurRepository;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                PasswordEncoder passwordEncoder,
                                RoleRepository roleRepository) {
    this.utilisateurRepository = utilisateurRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
  }

  @Override
  public Utilisateur registerUtilisateur(String pseudo, String mdp, String email) {
    if (pseudo == null || mdp == null || email == null) return null;

    // vérifie l’unicité du pseudo
    if (utilisateurRepository.findByPseudo(pseudo).isPresent()) {
      return null;
    }

    Utilisateur u = new Utilisateur();
    u.setPseudo(pseudo);
    u.setMdp(passwordEncoder.encode(mdp)); // hash BCrypt
    u.setEmail(email);
    u.setEnabled(true);

     Role roleUser = roleRepository.findByNom("ROLE_USER")
        .orElseThrow(() -> new IllegalStateException("ROLE_USER manquant en base"));
    u.setRole(roleUser);

    
    return utilisateurRepository.save(u);
  }

  @Override
  public Utilisateur authenticate(String pseudo, String mdp) {
    // compare le mot de passe avec PasswordEncoder (pas en clair)
    return utilisateurRepository.findByPseudo(pseudo)
        .filter(u -> passwordEncoder.matches(mdp, u.getMdp()))
        .orElse(null);
  }

  @Override
public Utilisateur creerAdmin(String pseudo, String mdp, String email) {
    if (pseudo == null || mdp == null || email == null) return null;

    // 1. Vérification de l'unicité
    if (utilisateurRepository.findByPseudo(pseudo).isPresent()) {
        // Idéalement, lancez ici une exception personnalisée (ex: PseudoDejaPrisException)
        return null; 
    }

    Utilisateur u = new Utilisateur();
    // Le 'login' du formulaire est le 'pseudo' de l'entité
    u.setPseudo(pseudo);
    u.setEmail(email);
    // Le 'motDePasse' du formulaire est haché en 'mdp' pour l'entité
    u.setMdp(passwordEncoder.encode(mdp)); 
    u.setEnabled(true);

    // 2. Récupération du Rôle ADMIN
    // Assurez-vous que "ROLE_ADMIN" existe dans votre table Role !
    Role roleAdmin = roleRepository.findByNom("ROLE_ADMIN")
         .orElseThrow(() -> new IllegalStateException("Le Rôle 'ROLE_ADMIN' est manquant. Vérifiez la base de données."));
         
    u.setRole(roleAdmin); 

    // 3. Sauvegarde en base de données
    return utilisateurRepository.save(u); // <-- C'est l'insertion finale !
  }
  @Override
  public Optional<Utilisateur> findByPseudo(String pseudo) {
    return utilisateurRepository.findByPseudo(pseudo);
  }

  @Override
  public List<Utilisateur> list() {
    return utilisateurRepository.findAll();
  }

  @Override
  public void supprimer(Utilisateur utilisateur) {
    utilisateurRepository.delete(utilisateur);
  }

  @Override
  public void modifier(Utilisateur utilisateur) {
    utilisateurRepository.save(utilisateur);
  }
}
