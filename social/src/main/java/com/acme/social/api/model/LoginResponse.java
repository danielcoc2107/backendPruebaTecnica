// src/main/java/com/acme/social/api/model/LoginResponse.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticacion")
public record LoginResponse(
        @Schema(description = "JWT de acceso")
        String accessToken,
        @Schema(example = "Bearer")
        String tokenType,
        @Schema(description = "Tiempo de expiracion en segundos", example = "7200")
        long expiresInSeconds
) {}
