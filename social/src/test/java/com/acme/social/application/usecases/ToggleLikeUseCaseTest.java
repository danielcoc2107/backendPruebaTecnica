// src/test/java/com/acme/social/application/usecases/ToggleLikeUseCaseTest.java
package com.acme.social.application.usecases;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.ports.LikeCommandPort;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToggleLikeUseCaseTest {

    @Test
    void shouldToggleLike() {
        LikeCommandPort port = mock(LikeCommandPort.class);
        ToggleLikeUseCase useCase = new ToggleLikeUseCase(port);

        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        when(port.toggleLike(userId, postId)).thenReturn(new ToggleLikeResult(true, 10));

        ToggleLikeResult result = useCase.execute(userId, postId);

        assertTrue(result.liked());
        assertEquals(10, result.totalLikes());
        verify(port).toggleLike(userId, postId);
    }
}
