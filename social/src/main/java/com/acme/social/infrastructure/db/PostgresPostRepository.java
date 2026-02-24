// src/main/java/com/acme/social/infrastructure/db/PostgresPostRepository.java
package com.acme.social.infrastructure.db;

import com.acme.social.application.dto.CreatePostResult;
import com.acme.social.application.ports.PostCommandPort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Repository
public class PostgresPostRepository implements PostCommandPort {

    private final NamedParameterJdbcTemplate jdbc;

    public PostgresPostRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public CreatePostResult createPost(UUID userId, String message) {
        String sql = "SELECT * FROM sp_crear_publicacion(:user_id, :message)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("message", message);

        return jdbc.queryForObject(sql, params, (rs, rowNum) -> {
            UUID postId = rs.getObject("publicacion_id", UUID.class);
            Timestamp ts = rs.getTimestamp("fecha_publicacion");
            OffsetDateTime publishedAt = ts.toInstant().atOffset(ZoneOffset.UTC);
            return new CreatePostResult(postId, publishedAt);
        });
    }
}
