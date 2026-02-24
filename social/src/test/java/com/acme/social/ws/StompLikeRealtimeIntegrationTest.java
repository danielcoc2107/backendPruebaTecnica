// src/test/java/com/acme/social/ws/StompLikeRealtimeIntegrationTest.java
package com.acme.social.ws;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import com.acme.social.security.jwt.JwtService;
import com.acme.social.ws.dto.LikeUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.*;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "security.jwt.secret=CAMBIA_ESTE_SECRET_LARGO_DE_32+_CARACTERES_1234567890_ABCDEFG"
        })
class StompLikeRealtimeIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JwtService jwtService;

    @MockBean
    ToggleLikeUseCase toggleLikeUseCase;

    @Test
    void shouldReceiveLikeUpdatedEventOverStomp() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        // mock del caso de uso
        Mockito.when(toggleLikeUseCase.execute(eq(userId), eq(postId)))
                .thenReturn(new ToggleLikeResult(true, 10));

        String token = jwtService.generateToken(
                userId.toString(),
                "daniel_alias",
                "daniel_username",
                Duration.ofHours(2)
        );

        WebSocketStompClient stompClient = buildStompClient();

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + token);
        connectHeaders.add("X-Correlation-Id", "test-corr-123");

        CompletableFuture<LikeUpdatedEvent> received = new CompletableFuture<>();

        ListenableFuture<StompSession> sessionFuture =
                stompClient.connectAsync("ws://localhost:" + port + "/ws", connectHeaders, new StompSessionHandlerAdapter() {});

        StompSession session = sessionFuture.get(3, TimeUnit.SECONDS);
        assertTrue(session.isConnected());

        String topic = "/topic/posts/" + postId + "/likes";
        session.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return LikeUpdatedEvent.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                received.complete((LikeUpdatedEvent) payload);
            }
        });

        // Enviar toggle like
        session.send("/app/likes/toggle", new com.acme.social.ws.dto.ToggleLikeWsRequest(postId));

        LikeUpdatedEvent event = received.get(3, TimeUnit.SECONDS);

        assertEquals(postId, event.postId());
        assertTrue(event.liked());
        assertEquals(10, event.totalLikes());

        session.disconnect();
    }

    private WebSocketStompClient buildStompClient() {
        var transports = List.<Transport>of(new WebSocketTransport(new StandardWebSocketClient()));
        var sockJsClient = new SockJsClient(transports);
        var stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }
}
