CREATE OR REPLACE FUNCTION sp_crear_publicacion(user_id UUID, message TEXT)
RETURNS TABLE (publicacion_id UUID, fecha_publicacion TIMESTAMP)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO publicaciones (id, usuario_id, mensaje, fecha_publicacion, created_at, updated_at)
    VALUES (gen_random_uuid(), user_id, message, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id, publicaciones.fecha_publicacion
    INTO publicacion_id, fecha_publicacion;

    RETURN NEXT;
END;
$$;

CREATE OR REPLACE FUNCTION sp_toggle_like(user_id UUID, post_id UUID)
RETURNS TABLE (liked BOOLEAN, total_likes BIGINT)
LANGUAGE plpgsql
AS $$
DECLARE
    existing_like_id UUID;
BEGIN
    SELECT id
    INTO existing_like_id
    FROM likes
    WHERE usuario_id = user_id
      AND publicacion_id = post_id;

    IF existing_like_id IS NULL THEN
        INSERT INTO likes (id, usuario_id, publicacion_id, created_at)
        VALUES (gen_random_uuid(), user_id, post_id, CURRENT_TIMESTAMP);
        liked := TRUE;
    ELSE
        DELETE FROM likes
        WHERE id = existing_like_id;
        liked := FALSE;
    END IF;

    SELECT COUNT(*)::BIGINT
    INTO total_likes
    FROM likes
    WHERE publicacion_id = post_id;

    RETURN NEXT;
END;
$$;

