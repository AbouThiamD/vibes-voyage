package com.descodeuses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    // Injection du bean @Component CustomAuthenticationSuccessHandler
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
               .requestMatchers("/Admin", "/Admin/**").hasRole("ADMIN") 
                .requestMatchers(
                    "/", "/login", "/register", "/error",
                    "/CSS/**","/css/**", "/JS/**","/js/**",
                    "/IMAGES/**","/images/**", "/webjars/**","/uploads/**","/files/**","/IMAGES/**","/CSS/**"
                ).permitAll()
                .requestMatchers("/card").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/")                        // ta page de login
                .loginProcessingUrl("/perform_login")  // action du <form>
                .usernameParameter("pseudo")           // doit matcher le name du champ
                .passwordParameter("mdp")              // idem
                .successHandler(successHandler)        // <-- utilise le bean injecté
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
