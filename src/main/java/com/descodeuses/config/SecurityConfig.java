package com.descodeuses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(); // redirige /Admin ou /card
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // OK en dev; active-le en prod + token dans le form
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/Admin").hasRole("ADMIN")
                .requestMatchers(
                    "/", "/login", "/register", "/error",
                    "/CSS/**","/css/**", "/JS/**","/js/**",
                    "/IMAGES/**","/images/**", "/webjars/**"
                ).permitAll()
                .requestMatchers("/card").authenticated() // ← /card nécessite login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/")                        // ta page avec le popup
                .loginProcessingUrl("/perform_login")  // POST du form
                .usernameParameter("pseudo")
                .passwordParameter("mdp")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
