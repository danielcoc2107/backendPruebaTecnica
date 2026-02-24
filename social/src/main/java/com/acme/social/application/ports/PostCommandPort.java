// src/main/java/com/acme/social/application/ports/PostCommandPort.java
package com.acme.social.application.ports;

import com.acme.social.application.dto.CreatePostResult;

import java.util.UUID;

public interface PostCommandPort {
    CreatePostResult createPost(UUID userId, String message);
}
