package com.descodeuses.voyage.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.repository.UtilisateurRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
        // Recherche l'utilisateur par son pseudo en utilisant la méthode du repository
        Optional<Utilisateur> user = utilisateurRepository.findByPseudo(pseudo);
        
        if (user.isPresent()) {
            // ✅ Construire un objet UserDetails à partir de l'entité Utilisateur
            Utilisateur utilisateur = user.get();
            
            // Assurez-vous que le rôle n'est pas nul avant de l'utiliser
            String roleNom = utilisateur.getRole() != null && utilisateur.getRole().getNom() != null ?
                             utilisateur.getRole().getNom() : "ROLE_USER"; // Rôle par défaut si non défini

            // Crée une autorité à partir du nom du rôle (ex: "ROLE_ADMIN")
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleNom);

            // Retourne un nouvel objet UserDetails que Spring comprend
            return new User(
                utilisateur.getPseudo(),
                utilisateur.getMdp(),
                Collections.singletonList(authority)
            );
        } else {
            throw new UsernameNotFoundException("Aucun utilisateur trouvé avec le pseudo : " + pseudo);
        }
    }
}
