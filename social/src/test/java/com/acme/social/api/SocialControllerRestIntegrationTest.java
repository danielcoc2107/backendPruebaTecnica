package com.acme.social.api;

import com.acme.social.config.SecurityConfig;
import com.acme.social.application.dto.CreatePostRequest;
import com.acme.social.application.dto.CreatePostResult;
import com.acme.social.application.usecases.CreatePostUseCase;
import com.acme.social.application.usecases.ListPostsUseCase;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import com.acme.social.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SocialController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class SocialControllerRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreatePostUseCase createPostUseCase;

    @MockBean
    private ToggleLikeUseCase toggleLikeUseCase;

    @MockBean
    private ListPostsUseCase listPostsUseCase;

    @MockBean
    private JwtService jwtService;

    @Test
    void createPostShouldUseAuthenticatedUserId() throws Exception {
        UUID userId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID postId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1");
        OffsetDateTime publishedAt = OffsetDateTime.parse("2026-02-24T10:00:00Z");
        String token = "test.jwt.token";

        when(createPostUseCase.execute(any(CreatePostRequest.class)))
                .thenReturn(new CreatePostResult(postId, publishedAt));
        when(jwtService.isValid(token)).thenReturn(true);
        when(jwtService.extractSubject(token)).thenReturn(userId.toString());

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "message": "Hola desde test"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId.toString()))
                .andExpect(jsonPath("$.publishedAt").value("2026-02-24T10:00:00Z"));

        ArgumentCaptor<CreatePostRequest> captor = ArgumentCaptor.forClass(CreatePostRequest.class);
        verify(createPostUseCase).execute(captor.capture());

        assertEquals(userId, captor.getValue().userId());
        assertEquals("Hola desde test", captor.getValue().message());
    }
}
