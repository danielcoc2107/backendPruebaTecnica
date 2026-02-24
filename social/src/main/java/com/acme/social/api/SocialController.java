// src/main/java/com/acme/social/api/SocialController.java
package com.acme.social.api;

import com.acme.social.api.model.CreatePostHttpRequest;
import com.acme.social.api.model.CreatePostHttpResponse;
import com.acme.social.api.model.ToggleLikeHttpRequest;
import com.acme.social.api.model.ToggleLikeHttpResponse;
import com.acme.social.application.dto.CreatePostRequest;
import com.acme.social.application.dto.PostResponse;
import com.acme.social.application.usecases.CreatePostUseCase;
import com.acme.social.application.usecases.ListPostsUseCase;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Social", description = "Publicaciones y likes")
public class SocialController {

    private static final Logger log = LoggerFactory.getLogger(SocialController.class);

    private final CreatePostUseCase createPostUseCase;
    private final ToggleLikeUseCase toggleLikeUseCase;
    private final ListPostsUseCase listPostsUseCase;

    public SocialController(
            CreatePostUseCase createPostUseCase,
            ToggleLikeUseCase toggleLikeUseCase,
            ListPostsUseCase listPostsUseCase
    ) {
        this.createPostUseCase = Objects.requireNonNull(createPostUseCase);
        this.toggleLikeUseCase = Objects.requireNonNull(toggleLikeUseCase);
        this.listPostsUseCase = Objects.requireNonNull(listPostsUseCase);
    }

    @PostMapping("/posts")
    @Operation(summary = "Crear una publicación")
    public ResponseEntity<CreatePostHttpResponse> createPost(
            Authentication authentication,
            @Valid @RequestBody CreatePostHttpRequest body
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("HTTP createPost userId={}", userId);

        var result = createPostUseCase.execute(new CreatePostRequest(userId, body.message()));
        return ResponseEntity.ok(new CreatePostHttpResponse(result.postId(), result.publishedAt()));
    }

    @GetMapping("/posts")
    @Operation(summary = "Listar publicaciones")
    public ResponseEntity<List<PostResponse>> listPosts(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("HTTP listPosts userId={}", userId);
        return ResponseEntity.ok(listPostsUseCase.execute(userId));
    }

    @PostMapping("/likes/toggle")
    @Operation(summary = "Dar/Quitar like a una publicación")
    public ResponseEntity<ToggleLikeHttpResponse> toggleLike(
            Authentication authentication,
            @Valid @RequestBody ToggleLikeHttpRequest body
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        log.info("HTTP toggleLike userId={} postId={}", userId, body.postId());

        var result = toggleLikeUseCase.execute(userId, body.postId());
        return ResponseEntity.ok(new ToggleLikeHttpResponse(result.liked(), result.totalLikes()));
    }
}
