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
    public Utilisateur save(Utilisateur utilisateur) {
    return utilisateurRepository.save(utilisateur);
}

  @Override
  public Utilisateur registerUtilisateur(String pseudo, String mdp, String email) {
    if (pseudo == null || pseudo.isBlank() ||
        mdp == null || mdp.isBlank() ||
        email == null || email.isBlank()) {
      throw new IllegalArgumentException("Champs requis manquants");
    }

    if (utilisateurRepository.existsByPseudoIgnoreCase(pseudo)) {
      throw new IllegalStateException("Pseudo déjà pris");
    }

    Utilisateur u = new Utilisateur();
    u.setPseudo(pseudo.trim());
    u.setEmail(email.trim());
    u.setMdp(passwordEncoder.encode(mdp)); // BCrypt
    u.setEnabled(true);


    Role roleUser = roleRepository.findByNom("ROLE_USER")
        .orElseThrow(() -> new IllegalStateException("Rôle USER manquant en base"));
    

    u.setRole(roleUser);
    return utilisateurRepository.save(u);
  }

  /** Utile seulement si tu fais une auth "maison".
      Si tu utilises Spring Security (formLogin), tu peux ignorer cette méthode. */
  @Override
  public Utilisateur authenticate(String pseudo, String mdp) {
    return utilisateurRepository.findFirstByPseudoIgnoreCaseOrderByIdAsc(pseudo)
        .filter(u -> passwordEncoder.matches(mdp, u.getMdp()))
        .orElse(null);
  }

  


  @Override
  public Utilisateur creerAdmin(String pseudo, String mdp, String email) {
    if (pseudo == null || pseudo.isBlank() ||
        mdp == null || mdp.isBlank() ||
        email == null || email.isBlank()) {
      throw new IllegalArgumentException("Champs requis manquants");
    }
    if (utilisateurRepository.existsByPseudoIgnoreCase(pseudo)) {
      throw new IllegalStateException("Pseudo déjà pris");
    }

    Utilisateur u = new Utilisateur();
    u.setPseudo(pseudo.trim());
    u.setEmail(email.trim());
    u.setMdp(passwordEncoder.encode(mdp));
    u.setEnabled(true);

    // ➜ Si base = 'ADMIN'/'USER' :
    Role roleAdmin = roleRepository.findByNom("ADMIN")
        .orElseThrow(() -> new IllegalStateException("Rôle ADMIN manquant en base"));
    // ➜ Si base = 'ROLE_ADMIN', adapte en conséquence.

    u.setRole(roleAdmin);
    return utilisateurRepository.save(u);
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
  public Optional<Utilisateur> findById(long id) {
  return utilisateurRepository.findById(id);
}

  @Override
  public void modifier(Utilisateur utilisateur) {
    utilisateurRepository.save(utilisateur);
  }
  


  
}
