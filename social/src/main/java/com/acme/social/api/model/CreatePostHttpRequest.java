// src/main/java/com/acme/social/api/model/CreatePostHttpRequest.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload para crear una publicacion")
public record CreatePostHttpRequest(
        @NotBlank
        @Schema(description = "Mensaje de la publicacion", example = "Hola mundo")
        String message
) {}
