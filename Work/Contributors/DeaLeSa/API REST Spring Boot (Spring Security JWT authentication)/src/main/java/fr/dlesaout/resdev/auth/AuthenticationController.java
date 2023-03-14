package fr.dlesaout.resdev.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", name = "_register")
    public ResponseEntity<AuthenticationResponse> inscription(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.inscription(request));
    }

    @PostMapping(value = "/login", name = "_login")
    public ResponseEntity<AuthenticationResponse> inscription(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.connexion(request));
    }

}
