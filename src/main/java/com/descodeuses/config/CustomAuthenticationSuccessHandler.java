package com.descodeuses.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("Connexion réussie pour l'utilisateur : {}", authentication.getName());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String targetUrl = "/card";

        for (GrantedAuthority grantedAuthority : authorities) {
            logger.info("Rôle détecté : {}", grantedAuthority.getAuthority());
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
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
