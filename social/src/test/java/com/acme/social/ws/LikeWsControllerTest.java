// src/test/java/com/acme/social/ws/LikeWsControllerTest.java
package com.acme.social.ws;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import com.acme.social.ws.dto.ToggleLikeWsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.UUID;

import static org.mockito.Mockito.*;

class LikeWsControllerTest {

    @Test
    void shouldBroadcastLikeUpdatedEvent() {
        ToggleLikeUseCase useCase = mock(ToggleLikeUseCase.class);
        SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

        LikeWsController controller = new LikeWsController(useCase, template);

        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        when(useCase.execute(userId, postId)).thenReturn(new ToggleLikeResult(true, 7));

        Principal principal = () -> userId.toString();

        controller.toggleLike(new ToggleLikeWsRequest(postId), principal);

        verify(template).convertAndSend(
                eq("/topic/posts/" + postId + "/likes"),
                argThat(ev -> {
                    // ev es LikeUpdatedEvent pero lo evaluamos por propiedades via toString simple
                    return ev.toString().contains(postId.toString())
                            && ev.toString().contains("liked=true")
                            && ev.toString().contains("totalLikes=7");
                })
        );
    }
}
