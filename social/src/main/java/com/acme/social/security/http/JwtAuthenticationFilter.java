// src/main/java/com/acme/social/security/http/JwtAuthenticationFilter.java
package com.acme.social.security.http;

import com.acme.social.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Optional<String> tokenOpt = extractBearerToken(request.getHeader("Authorization"));

            if (tokenOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {

                String token = tokenOpt.get();

                if (jwtService.isValid(token)) {

                    String subject = jwtService.extractSubject(token); // UUID del usuario

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("JWT autenticado correctamente para userId={}", subject);

                } else {
                    log.warn("JWT inválido recibido en request URI={}", request.getRequestURI());
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Error procesando JWT para URI={}", request.getRequestURI(), ex);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private Optional<String> extractBearerToken(String header) {
        if (header == null || header.isBlank()) {
            return Optional.empty();
        }

        if (!header.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = header.substring("Bearer ".length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }
}
