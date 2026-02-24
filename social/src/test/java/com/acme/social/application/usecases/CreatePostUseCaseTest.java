// src/test/java/com/acme/social/application/usecases/CreatePostUseCaseTest.java
package com.acme.social.application.usecases;

import com.acme.social.application.dto.CreatePostRequest;
import com.acme.social.application.dto.CreatePostResult;
import com.acme.social.application.ports.PostCommandPort;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePostUseCaseTest {

    @Test
    void shouldCreatePost() {
        PostCommandPort port = mock(PostCommandPort.class);
        CreatePostUseCase useCase = new CreatePostUseCase(port);

        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        when(port.createPost(eq(userId), eq("Hola"))).thenReturn(new CreatePostResult(postId, OffsetDateTime.now()));

        CreatePostResult result = useCase.execute(new CreatePostRequest(userId, "Hola"));

        assertEquals(postId, result.postId());
        verify(port).createPost(userId, "Hola");
    }

    @Test
    void shouldRejectBlankMessage() {
        PostCommandPort port = mock(PostCommandPort.class);
        CreatePostUseCase useCase = new CreatePostUseCase(port);

        UUID userId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new CreatePostRequest(userId, "   "))
        );
        verifyNoInteractions(port);
    }
}
