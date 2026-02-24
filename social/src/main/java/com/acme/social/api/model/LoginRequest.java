// src/main/java/com/acme/social/api/model/LoginRequest.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales de acceso")
public record LoginRequest(
        @Schema(example = "jdoe")
        @NotBlank String username,
        @Schema(example = "S3cr3t!")
        @NotBlank String password
) {}
