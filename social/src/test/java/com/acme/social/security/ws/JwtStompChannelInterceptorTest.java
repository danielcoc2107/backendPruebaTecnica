// src/test/java/com/acme/social/security/ws/JwtStompChannelInterceptorTest.java
package com.acme.social.security.ws;

import com.acme.social.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtStompChannelInterceptorTest {

    @Test
    void shouldSetPrincipalOnConnectWhenTokenValid() {
        JwtService jwtService = mock(JwtService.class);
        JwtStompChannelInterceptor interceptor = new JwtStompChannelInterceptor(jwtService);

        String token = "abc.def.ghi";
        when(jwtService.isValid(token)).thenReturn(true);
        when(jwtService.extractSubject(token)).thenReturn("11111111-1111-1111-1111-111111111111");

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", "Bearer " + token);

        Message<byte[]> msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        Message<?> out = interceptor.preSend(msg, null);

        StompHeaderAccessor outAccessor = StompHeaderAccessor.wrap(out);
        assertNotNull(outAccessor.getUser());
        assertEquals("11111111-1111-1111-1111-111111111111", outAccessor.getUser().getName());
    }

    @Test
    void shouldThrowWhenTokenMissing() {
        JwtService jwtService = mock(JwtService.class);
        JwtStompChannelInterceptor interceptor = new JwtStompChannelInterceptor(jwtService);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        Message<byte[]> msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        assertThrows(IllegalArgumentException.class, () -> interceptor.preSend(msg, null));
    }
}
