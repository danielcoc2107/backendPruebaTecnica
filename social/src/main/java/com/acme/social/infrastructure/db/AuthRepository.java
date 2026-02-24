// src/main/java/com/acme/social/infrastructure/db/AuthRepository.java
package com.acme.social.infrastructure.db;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthRepository {

    public record UserAuthView(UUID id, String alias, String username, String passwordHash) {}

    private final NamedParameterJdbcTemplate jdbc;

    public AuthRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<UserAuthView> findByUsername(String username) {
        String sql = """
            SELECT id, alias, username, password_hash
            FROM usuarios
            WHERE username = :username
              AND deleted_at IS NULL
            """;

        return jdbc.query(sql, Map.of("username", username), rs -> {
            if (!rs.next()) return Optional.empty();
            return Optional.of(new UserAuthView(
                    rs.getObject("id", UUID.class),
                    rs.getString("alias"),
                    rs.getString("username"),
                    rs.getString("password_hash")
            ));
        });
    }
}
