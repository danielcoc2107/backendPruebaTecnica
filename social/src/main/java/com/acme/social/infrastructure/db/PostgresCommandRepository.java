// src/main/java/com/acme/social/infrastructure/db/PostgresCommandRepository.java
package com.acme.social.infrastructure.db;

import com.acme.social.application.dto.CreatePostResult;
import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.ports.LikeCommandPort;
import com.acme.social.application.ports.PostCommandPort;
import com.acme.social.infrastructure.db.mappers.CreatePostResultRowMapper;
import com.acme.social.infrastructure.db.mappers.ToggleLikeResultRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
public class PostgresCommandRepository implements PostCommandPort, LikeCommandPort {

    private final NamedParameterJdbcTemplate jdbc;

    public PostgresCommandRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public CreatePostResult createPost(UUID userId, String message) {
        String sql = "SELECT * FROM sp_crear_publicacion(:user_id, :message)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("message", message);

        return jdbc.queryForObject(sql, params, new CreatePostResultRowMapper());
    }

    @Override
    public ToggleLikeResult toggleLike(UUID userId, UUID postId) {
        String sql = "SELECT * FROM sp_toggle_like(:user_id, :post_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("post_id", postId);

        return jdbc.queryForObject(sql, params, new ToggleLikeResultRowMapper());
    }
}
