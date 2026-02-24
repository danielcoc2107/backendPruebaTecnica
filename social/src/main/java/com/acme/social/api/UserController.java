package com.acme.social.api;

import com.acme.social.api.model.ProfileResponse;
import com.acme.social.infrastructure.jpa.UsuarioJpaRepository;
import com.acme.social.infrastructure.jpa.UsuarioEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Perfil de usuario autenticado")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UsuarioJpaRepository usuarioJpaRepository;

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ProfileResponse getProfile(Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        log.info("HTTP getProfile userId={}", userId);

        UsuarioEntity user = usuarioJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new ProfileResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getAlias(),
                user.getFechaNacimiento()
        );
    }
}
