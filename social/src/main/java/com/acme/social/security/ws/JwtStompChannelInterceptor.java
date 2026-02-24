// src/main/java/com/acme/social/security/ws/JwtStompChannelInterceptor.java
package com.acme.social.security.ws;

import com.acme.social.observability.CorrelationIdFilter;
import com.acme.social.security.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtStompChannelInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtStompChannelInterceptor.class);

    private final JwtService jwtService;

    public JwtStompChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        // Correlation ID (si viene, lo tomamos; si no, generamos)
        String correlationId = Optional.ofNullable(accessor.getFirstNativeHeader(CorrelationIdFilter.HEADER))
                .filter(v -> !v.isBlank())
                .orElse(UUID.randomUUID().toString());

        MDC.put(CorrelationIdFilter.MDC_KEY, correlationId);

        try {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String authHeader = accessor.getFirstNativeHeader("Authorization");
                Optional<String> tokenOpt = extractBearerToken(authHeader);

                if (tokenOpt.isEmpty() || !jwtService.isValid(tokenOpt.get())) {
                    throw new IllegalArgumentException("JWT inválido o ausente en CONNECT");
                }

                String subject = jwtService.extractSubject(tokenOpt.get()); // UUID string

                Principal principal = new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

                accessor.setUser(principal);
                log.info("WS CONNECT OK userId={}", subject);
            }

            return message;
        } finally {
            // limpiamos para no “contaminar” hilos
            MDC.remove(CorrelationIdFilter.MDC_KEY);
        }
    }

    private Optional<String> extractBearerToken(String header) {
        if (header == null) return Optional.empty();
        if (!header.startsWith("Bearer ")) return Optional.empty();
        String token = header.substring("Bearer ".length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }
}
