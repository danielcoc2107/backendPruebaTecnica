package com.acme.social.infrastructure.db;

import com.acme.social.application.dto.RegisterResult;
import com.acme.social.application.exceptions.DuplicateUserException;
import com.acme.social.application.ports.RegisterUserPort;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Repository
public class PostgresRegisterUserRepository implements RegisterUserPort {

    private static final String DUPLICATE_SQLSTATE = "P0001";

    private final NamedParameterJdbcTemplate jdbc;

    public PostgresRegisterUserRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public RegisterResult register(
            String username,
            String passwordHash,
            String nombres,
            String apellidos,
            LocalDate fechaNacimiento,
            String alias
    ) {
        String sql = """
                SELECT * FROM sp_register_user(
                    :p_username,
                    :p_password_hash,
                    :p_nombres,
                    :p_apellidos,
                    :p_fecha_nacimiento,
                    :p_alias
                )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_username", username)
                .addValue("p_password_hash", passwordHash)
                .addValue("p_nombres", nombres)
                .addValue("p_apellidos", apellidos)
                .addValue("p_fecha_nacimiento", fechaNacimiento)
                .addValue("p_alias", alias);

        try {
            return jdbc.queryForObject(sql, params, (rs, rowNum) -> new RegisterResult(
                    rs.getObject("user_id", UUID.class),
                    rs.getString("alias")
            ));
        } catch (DataAccessException ex) {
            throw translateDuplicate(ex);
        }
    }

    private RuntimeException translateDuplicate(DataAccessException ex) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
        if (root instanceof SQLException sqlEx && DUPLICATE_SQLSTATE.equals(sqlEx.getSQLState())) {
            String msg = sqlEx.getMessage() == null ? "" : sqlEx.getMessage();
            if (msg.contains("USERNAME_ALREADY_EXISTS")) {
                return new DuplicateUserException("El username ya existe");
            }
            if (msg.contains("ALIAS_ALREADY_EXISTS")) {
                return new DuplicateUserException("El alias ya existe");
            }
            return new DuplicateUserException("Usuario duplicado");
        }
        return ex;
    }
}
