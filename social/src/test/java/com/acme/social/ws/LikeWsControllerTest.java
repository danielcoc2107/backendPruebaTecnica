// src/test/java/com/acme/social/ws/LikeWsControllerTest.java
package com.acme.social.ws;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import com.acme.social.ws.dto.LikeUpdatedEvent;
import com.acme.social.ws.dto.ToggleLikeWsRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

        String expectedTopic = "/topic/posts/" + postId + "/likes";

        ArgumentCaptor<LikeUpdatedEvent> captor = ArgumentCaptor.forClass(LikeUpdatedEvent.class);

        verify(template).convertAndSend(eq(expectedTopic), captor.capture());

        LikeUpdatedEvent sent = captor.getValue();
        assertEquals(postId, sent.postId());
        assertTrue(sent.liked());
        assertEquals(7, sent.totalLikes());
    }
}
