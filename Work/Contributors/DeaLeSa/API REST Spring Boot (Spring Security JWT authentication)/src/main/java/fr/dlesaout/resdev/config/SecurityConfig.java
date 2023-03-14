package fr.dlesaout.resdev.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Filtre qui intercepte les requêtes HTTP et vérifie la validité des jetons JWT.
    private final AuthenticationProvider authenticationProvider; // Interface qui fournit les fonctionnalités d'authentification.

    // Définit une chaîne de filtres de sécurité personnalisée pour l'application Spring Boot.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable() // Désactive la protection CSRF pour permettre les requêtes HTTP sans jeton CSRF.
                .authorizeHttpRequests()
                .requestMatchers( // Autorise les requêtes HTTP vers les URL spécifiées sans nécessiter d'authentification.
                        "/api/v1/auth/register",
                        "/api/v1/auth/login",
                        "/swagger-ui/*",
                        "/v3/api-docs",
                        "/v3/api-docs/*")
                .permitAll()
                .anyRequest()
                .authenticated() // Exige que toutes les autres requêtes HTTP soient authentifiées.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Indique que l'application Spring Boot ne doit pas créer de sessions.
                .and()
                .authenticationProvider(authenticationProvider) // Définit l'interface d'authentification à utiliser.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre de vérification des jetons JWT avant le filtre de connexion par nom d'utilisateur et mot de passe.
        return http.build(); // Retourne la chaîne de filtres de sécurité personnalisée.
    }
}
