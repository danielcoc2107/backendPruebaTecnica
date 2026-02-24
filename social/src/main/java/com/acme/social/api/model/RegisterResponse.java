package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record RegisterResponse(
        @Schema(description = "ID del usuario creado")
        UUID id,
        @Schema(description = "Alias del usuario creado")
        String alias
) {}
