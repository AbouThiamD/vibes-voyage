package com.descodeuses.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.descodeuses.voyage.repository.UtilisateurRepository;
import com.descodeuses.voyage.model.Utilisateur;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UtilisateurRepository utilisateurRepository;

    public CustomAuthenticationSuccessHandler(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("Connexion réussie pour l'utilisateur : {}", authentication.getName());

        
        Utilisateur user = utilisateurRepository.findByPseudo(authentication.getName()).orElse(null);
        if (user != null) {
            request.getSession().setAttribute("authenticatedUser", user);
            logger.info("Utilisateur placé en session: {}", user.getPseudo());
        } else {
            logger.warn("Aucun Utilisateur trouvé en BDD pour {}", authentication.getName());
        }

       
        String targetUrl = "/card";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority ga : authorities) {
            logger.info("Rôle détecté : {}", ga.getAuthority());
            if ("ROLE_ADMIN".equals(ga.getAuthority())) {
                targetUrl = "/Admin";
                break;
            }
        }

        logger.info("Redirection vers l'URL : {}", targetUrl);
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            logger.warn("La réponse est déjà validée. Impossible d'effectuer la redirection.");
        }
    }
}
