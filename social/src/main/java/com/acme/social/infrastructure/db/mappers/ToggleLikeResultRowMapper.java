// src/main/java/com/acme/social/infrastructure/db/mappers/ToggleLikeResultRowMapper.java
package com.acme.social.infrastructure.db.mappers;

import com.acme.social.application.dto.ToggleLikeResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ToggleLikeResultRowMapper implements RowMapper<ToggleLikeResult> {
    @Override
    public ToggleLikeResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ToggleLikeResult(
                rs.getBoolean("liked"),
                rs.getLong("total_likes")
        );
    }
}
