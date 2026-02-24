// src/main/java/com/acme/social/infrastructure/db/PostgresLikeRepository.java
package com.acme.social.infrastructure.db;

import com.acme.social.application.dto.ToggleLikeResult;
import com.acme.social.application.ports.LikeCommandPort;
import com.acme.social.infrastructure.db.mappers.ToggleLikeResultRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
public class PostgresLikeRepository implements LikeCommandPort {

    private static final Logger log = LoggerFactory.getLogger(PostgresLikeRepository.class);

    private final NamedParameterJdbcTemplate jdbc;

    public PostgresLikeRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public ToggleLikeResult toggleLike(UUID userId, UUID postId) {
        String sql = "SELECT * FROM sp_toggle_like(:user_id, :post_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("post_id", postId);

        ToggleLikeResult result = jdbc.queryForObject(sql, params, new ToggleLikeResultRowMapper());
        log.info("toggleLike userId={} postId={} liked={} totalLikes={}",
                userId, postId, result.liked(), result.totalLikes());

        return result;
    }
}
