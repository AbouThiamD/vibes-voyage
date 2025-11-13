package com.descodeuses.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
// --- Nouveaux Imports ---
import com.descodeuses.voyage.model.Categorie;
import com.descodeuses.voyage.repository.CategorieRepository; 
import com.descodeuses.voyage.model.Role; 
import com.descodeuses.voyage.repository.RoleRepository;
// --- Fin des Imports ---
import com.descodeuses.voyage.model.Utilisateur;
import com.descodeuses.voyage.repository.UtilisateurRepository;

import java.util.Optional; 

@Component
public class InitialDataLoader implements CommandLineRunner {

    // Injection des repositories
    private final RoleRepository roleRepository; 
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategorieRepository categorieRepository; // AJOUTÉ

    public InitialDataLoader(RoleRepository roleRepository, 
                             UtilisateurRepository utilisateurRepository, 
                             PasswordEncoder passwordEncoder,
                             CategorieRepository categorieRepository) { // AJOUTÉ AU CONSTRUCTEUR
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.categorieRepository = categorieRepository; // Initialisation
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // --- 1. CRÉATION DES RÔLES ET UTILISATEURS ---
        Role adminRole = findOrCreateRole("ADMIN");
        Role userRole = findOrCreateRole("USER"); 

        createDefaultUser("admin", "admin@voyage.com", "motdepasse123", adminRole);
        createDefaultUser("utilisateur", "user@voyage.com", "test1234", userRole);
        
        // --- 2. CRÉATION DES CATÉGORIES/CONTINENTS ---
        findOrCreateCategorie("Afrique");
        findOrCreateCategorie("Amérique du Nord");
        findOrCreateCategorie("Amérique du Sud");
        findOrCreateCategorie("Asie");
        findOrCreateCategorie("Europe");
        findOrCreateCategorie("Océanie");
    }

    // --- LOGIQUE RÔLES (inchangée) ---
    private Role findOrCreateRole(String nom) {
        return roleRepository.findByNom(nom)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setNom(nom);
                    return roleRepository.save(newRole);
                });
    }

    private void createDefaultUser(String pseudo, String email, String password, Role role) {
        Optional<Utilisateur> existingUser = utilisateurRepository.findByPseudo(pseudo);
        if (existingUser.isEmpty()) {
            Utilisateur user = new Utilisateur();
            user.setPseudo(pseudo);
            user.setEmail(email);
            user.setMdp(passwordEncoder.encode(password)); 
            user.setRole(role); 
            utilisateurRepository.save(user);
            System.out.println(">>> Utilisateur " + role.getNom() + " créé: " + pseudo + "/" + password);
        }
    }
    
    // --- NOUVELLE LOGIQUE : CRÉATION DES CATÉGORIES ---
    private void findOrCreateCategorie(String nom) {
        // Utilise la méthode findByNomCategorieIgnoreCase de votre Repository
        categorieRepository.findByNomCategorieIgnoreCase(nom) 
                .orElseGet(() -> {
                    Categorie newCat = new Categorie();
                    newCat.setNomCategorie(nom);
                    return categorieRepository.save(newCat);
                });
    }
}