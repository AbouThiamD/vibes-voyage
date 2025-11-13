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
                .loginPage("/")                       
                .loginProcessingUrl("/perform_login")
                .usernameParameter("pseudo")           
                .passwordParameter("mdp")              
                .successHandler(successHandler)        
              .failureUrl("/error?login=1")   
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
