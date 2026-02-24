// src/main/java/com/acme/social/api/model/ToggleLikeHttpResponse.java
package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de operacion de like")
public record ToggleLikeHttpResponse(
        boolean liked,
        long totalLikes
) {}
