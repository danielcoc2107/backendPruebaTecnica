// src/main/java/com/acme/social/api/SocialController.java
package com.acme.social.api;

import com.acme.social.api.model.CreatePostHttpRequest;
import com.acme.social.api.model.CreatePostHttpResponse;
import com.acme.social.api.model.ToggleLikeHttpRequest;
import com.acme.social.api.model.ToggleLikeHttpResponse;
import com.acme.social.application.dto.CreatePostRequest;
import com.acme.social.application.usecases.CreatePostUseCase;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class SocialController {

    private final CreatePostUseCase createPostUseCase;
    private final ToggleLikeUseCase toggleLikeUseCase;

    public SocialController(CreatePostUseCase createPostUseCase, ToggleLikeUseCase toggleLikeUseCase) {
        this.createPostUseCase = Objects.requireNonNull(createPostUseCase);
        this.toggleLikeUseCase = Objects.requireNonNull(toggleLikeUseCase);
    }

    @PostMapping("/posts")
    public ResponseEntity<CreatePostHttpResponse> createPost(@RequestBody CreatePostHttpRequest body) {
        var result = createPostUseCase.execute(new CreatePostRequest(body.userId(), body.message()));
        return ResponseEntity.ok(new CreatePostHttpResponse(result.postId(), result.publishedAt()));
    }

    @PostMapping("/likes/toggle")
    public ResponseEntity<ToggleLikeHttpResponse> toggleLike(@RequestBody ToggleLikeHttpRequest body) {
        var result = toggleLikeUseCase.execute(body.userId(), body.postId());
        return ResponseEntity.ok(new ToggleLikeHttpResponse(result.liked(), result.totalLikes()));
    }
}
