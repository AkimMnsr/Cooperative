package fr.dlesaout.resdev.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indique que cette classe est un composant Spring qui doit être automatiquement découvert et configuré par l'application Spring Boot.
@RequiredArgsConstructor // Génère un constructeur avec tous les champs marqués avec l'annotation @NonNull.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Service qui fournit des fonctionnalités de création et de validation de jetons JWT.
    private final UserDetailsService userDetailsService; // Interface qui fournit les détails de l'utilisateur pour l'authentification.

    // Méthode qui s'exécute pour chaque requête HTTP une fois.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Récupère l'en-tête d'authentification de la requête HTTP.
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Vérifie si l'en-tête d'authentification est manquant ou ne commence pas par "Bearer ".
            filterChain.doFilter(request, response); // Passe au filtre suivant dans la chaîne de filtres de sécurité.
            return;
        }
        jwt = authHeader.substring(7); // Récupère le jeton JWT à partir de l'en-tête d'authentification.
        userEmail = jwtService.extractUsername(jwt); // Extrait le nom d'utilisateur à partir du jeton JWT.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Vérifie si le nom d'utilisateur est valide et si l'authentification n'a pas déjà été effectuée.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Récupère les détails de l'utilisateur à partir de l'interface UserDetailsService.
            if(jwtService.isTokenValid(jwt, userDetails)) { // Vérifie si le jeton JWT est valide pour les détails de l'utilisateur.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()); // Crée un jeton d'authentification à partir des détails de l'utilisateur et des autorisations associées.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                ); // Définit les détails d'authentification associés au jeton.
                SecurityContextHolder.getContext().setAuthentication(authToken); // Configure l'authentification dans le contexte de sécurité de l'application Spring Boot.
            }
        }
        filterChain.doFilter(request, response); // Passe au filtre suivant dans la chaîne de filtres de sécurité.
    }
}