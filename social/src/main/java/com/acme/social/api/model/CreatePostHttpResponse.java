// src/main/java/com/acme/social/api/model/CreatePostHttpResponse.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Respuesta de creacion de publicacion")
public record CreatePostHttpResponse(
        @Schema(format = "uuid")
        UUID postId,
        OffsetDateTime publishedAt
) {}
