package fr.dlesaout.resdev.auth;

import fr.dlesaout.resdev.config.JwtService;
import fr.dlesaout.resdev.entities.Role;
import fr.dlesaout.resdev.entities.Utilisateur;
import fr.dlesaout.resdev.exceptions.utilisateur.UserAlreadyExistsException;
import fr.dlesaout.resdev.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse inscription(RegisterRequest request) {
        var existingUser = utilisateurRepository.findUtilisateurByEmail(request.getEmail());
        if(existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Cette adresse email est déjà associée à un utilisateur.");
        }
        var user = Utilisateur.builder()
                .pseudo(request.getPseudo())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .rue(request.getRue())
                .codePostal(request.getCodePostal())
                .ville(request.getVille())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(Role.UTILISATEUR)
                .build();
        utilisateurRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse connexion(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );
        var user = utilisateurRepository.findUtilisateurByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
