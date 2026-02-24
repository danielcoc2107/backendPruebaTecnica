// src/main/java/com/acme/social/api/model/ToggleLikeHttpRequest.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Payload para dar o quitar like")
public record ToggleLikeHttpRequest(
        @NotNull
        @Schema(description = "ID de la publicacion", format = "uuid")
        UUID postId
) {}
