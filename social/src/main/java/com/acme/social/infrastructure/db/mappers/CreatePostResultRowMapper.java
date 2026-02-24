// src/main/java/com/acme/social/infrastructure/db/mappers/CreatePostResultRowMapper.java
package com.acme.social.infrastructure.db.mappers;

import com.acme.social.application.dto.CreatePostResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public final class CreatePostResultRowMapper implements RowMapper<CreatePostResult> {

    @Override
    public CreatePostResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID postId = rs.getObject("publicacion_id", UUID.class);

        // La SP retorna TIMESTAMP (sin TZ). Convertimos asumiendo UTC por defecto (recomendado).
        OffsetDateTime publishedAt = rs.getTimestamp("fecha_publicacion").toInstant().atOffset(ZoneOffset.UTC);

        return new CreatePostResult(postId, publishedAt);
    }
}
