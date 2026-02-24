// src/main/java/com/acme/social/application/ports/LikeCommandPort.java
package com.acme.social.application.ports;

import com.acme.social.application.dto.ToggleLikeResult;

import java.util.UUID;

public interface LikeCommandPort {
    ToggleLikeResult toggleLike(UUID userId, UUID postId);
}
