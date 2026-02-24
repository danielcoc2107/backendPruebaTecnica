// src/main/java/com/acme/social/api/AuthController.java
package com.acme.social.api;

import com.acme.social.api.model.LoginRequest;
import com.acme.social.api.model.LoginResponse;
import com.acme.social.infrastructure.db.AuthRepository;
import com.acme.social.security.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticacion y emision de JWT")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final Duration ACCESS_TTL = Duration.ofHours(2);

    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthRepository authRepository, JwtService jwtService) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y generar access token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        var user = authRepository.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (user.passwordHash() == null || user.passwordHash().isBlank()) {
            throw new IllegalArgumentException("Usuario sin password configurado");
        }

        if (!passwordEncoder.matches(req.password(), user.passwordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(
                user.id().toString(),
                user.alias(),
                user.username(),
                ACCESS_TTL
        );

        log.info("Login OK username={} userId={}", user.username(), user.id());

        return ResponseEntity.ok(new LoginResponse(token, "Bearer", ACCESS_TTL.toSeconds()));
    }
}
