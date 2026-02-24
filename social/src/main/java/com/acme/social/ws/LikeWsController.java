// src/main/java/com/acme/social/ws/LikeWsController.java
package com.acme.social.ws;

import com.acme.social.application.usecases.ToggleLikeUseCase;
import com.acme.social.ws.dto.LikeUpdatedEvent;
import com.acme.social.ws.dto.ToggleLikeWsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Controller
public class LikeWsController {

    private static final Logger log = LoggerFactory.getLogger(LikeWsController.class);

    private final ToggleLikeUseCase toggleLikeUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    public LikeWsController(ToggleLikeUseCase toggleLikeUseCase, SimpMessagingTemplate messagingTemplate) {
        this.toggleLikeUseCase = Objects.requireNonNull(toggleLikeUseCase);
        this.messagingTemplate = Objects.requireNonNull(messagingTemplate);
    }

    @MessageMapping("/likes/toggle") // cliente: /app/likes/toggle
    public void toggleLike(ToggleLikeWsRequest req, Principal principal) {
        if (req == null || req.postId() == null) throw new IllegalArgumentException("postId requerido");
        if (principal == null || principal.getName() == null) throw new IllegalStateException("No autenticado");

        UUID userId = UUID.fromString(principal.getName());
        UUID postId = req.postId();

        var result = toggleLikeUseCase.execute(userId, postId);

        var event = new LikeUpdatedEvent(postId, result.liked(), result.totalLikes());
        String topic = "/topic/posts/" + postId + "/likes";

        log.info("WS LikeUpdated topic={} userId={} liked={} totalLikes={}",
                topic, userId, result.liked(), result.totalLikes());

        messagingTemplate.convertAndSend(topic, event);
    }
}
