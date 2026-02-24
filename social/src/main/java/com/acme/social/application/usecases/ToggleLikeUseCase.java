// src/main/java/com/acme/social/application/usecases/ToggleLikeUseCase.java
package com.acme.social.application.usecases;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.ports.LikeCommandPort;

import java.util.Objects;
import java.util.UUID;

public final class ToggleLikeUseCase {

    private final LikeCommandPort likeCommandPort;

    public ToggleLikeUseCase(LikeCommandPort likeCommandPort) {
        this.likeCommandPort = Objects.requireNonNull(likeCommandPort);
    }

    public ToggleLikeResult execute(UUID userId, UUID postId) {
        if (userId == null) throw new IllegalArgumentException("userId requerido");
        if (postId == null) throw new IllegalArgumentException("postId requerido");
        return likeCommandPort.toggleLike(userId, postId);
    }
}
