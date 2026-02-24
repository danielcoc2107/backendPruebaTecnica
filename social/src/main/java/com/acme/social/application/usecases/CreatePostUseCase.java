// src/main/java/com/acme/social/application/usecases/CreatePostUseCase.java
package com.acme.social.application.usecases;

import com.acme.social.application.dto.CreatePostRequest;
import com.acme.social.application.dto.CreatePostResult;
import com.acme.social.application.ports.PostCommandPort;

import java.util.Objects;

public final class CreatePostUseCase {

    private final PostCommandPort postCommandPort;

    public CreatePostUseCase(PostCommandPort postCommandPort) {
        this.postCommandPort = Objects.requireNonNull(postCommandPort);
    }

    public CreatePostResult execute(CreatePostRequest req) {
        Objects.requireNonNull(req, "request no puede ser null");
        if (req.userId() == null) throw new IllegalArgumentException("userId es requerido");
        if (req.message() == null || req.message().trim().isEmpty()) {
            throw new IllegalArgumentException("message no puede estar vacío");
        }
        return postCommandPort.createPost(req.userId(), req.message().trim());
    }
}
