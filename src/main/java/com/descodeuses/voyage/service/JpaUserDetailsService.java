// package com.descodeuses.voyage.service;

// import java.util.Collections;
// import java.util.Optional;

// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.beans.factory.annotation.Autowired;

// import com.descodeuses.voyage.model.Utilisateur;
// import com.descodeuses.voyage.repository.UtilisateurRepository;

// @Service
// public class JpaUserDetailsService implements UserDetailsService {

//     @Autowired
//     private UtilisateurRepository utilisateurRepository;

//     @Override
//     public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
     
//         Optional<Utilisateur> user = utilisateurRepository.findByPseudo(pseudo);
        
//         if (user.isPresent()) {
          
//             Utilisateur utilisateur = user.get();
            
//             // Assurez-vous que le rôle n'est pas nul avant de l'utiliser
//             String roleNom = utilisateur.getRole() != null && utilisateur.getRole().getNom() != null ?
//                              utilisateur.getRole().getNom() : "ROLE_USER"; // Rôle par défaut si non défini

       
//             SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleNom);

          
//             return new User(
//                 utilisateur.getPseudo(),
//                 utilisateur.getMdp(),
//                 Collections.singletonList(authority)
//             );
//         } else {
//             throw new UsernameNotFoundException("Aucun utilisateur trouvé avec le pseudo : " + pseudo);
//         }
//     }
// }


package com.descodeuses.voyage.service;

import java.util.List;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.repository.UtilisateurRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public JpaUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
        Utilisateur u = utilisateurRepository.findByPseudo(pseudo)
            .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur trouvé avec le pseudo : " + pseudo));

       
        String rawRole = (u.getRole() != null && u.getRole().getNom() != null)
                ? u.getRole().getNom().toUpperCase()
                : "USER";

        String authority = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;

        return User.builder()
            .username(u.getPseudo())   
            .password(u.getMdp())      
            .authorities(List.of(new SimpleGrantedAuthority(authority))) 
            .build();
    }
}
