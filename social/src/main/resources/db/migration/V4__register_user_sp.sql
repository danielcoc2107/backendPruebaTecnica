CREATE OR REPLACE FUNCTION sp_register_user(
    p_username VARCHAR,
    p_password_hash VARCHAR,
    p_nombres VARCHAR,
    p_apellidos VARCHAR,
    p_fecha_nacimiento DATE,
    p_alias VARCHAR
)
RETURNS TABLE (user_id UUID, alias VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM usuarios u
        WHERE u.username = p_username
          AND u.deleted_at IS NULL
    ) THEN
        RAISE EXCEPTION 'USERNAME_ALREADY_EXISTS' USING ERRCODE = 'P0001';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM usuarios u
        WHERE u.alias = p_alias
          AND u.deleted_at IS NULL
    ) THEN
        RAISE EXCEPTION 'ALIAS_ALREADY_EXISTS' USING ERRCODE = 'P0001';
    END IF;

    INSERT INTO usuarios (
        id,
        username,
        password_hash,
        nombres,
        apellidos,
        fecha_nacimiento,
        alias,
        created_at,
        updated_at,
        deleted_at
    )
    VALUES (
        gen_random_uuid(),
        p_username,
        p_password_hash,
        p_nombres,
        p_apellidos,
        p_fecha_nacimiento,
        p_alias,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    )
    RETURNING usuarios.id, usuarios.alias
    INTO user_id, alias;

    RETURN NEXT;
END;
$$;
