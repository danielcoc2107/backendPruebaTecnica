INSERT INTO usuarios (
    id, username, password_hash, nombres, apellidos, fecha_nacimiento, alias, created_at, updated_at, deleted_at
) VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'ana',
        crypt('Ana12345!', gen_salt('bf')),
        'Ana',
        'Lopez',
        DATE '1995-04-12',
        'ana_l',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'bruno',
        crypt('Bruno12345!', gen_salt('bf')),
        'Bruno',
        'Diaz',
        DATE '1992-08-20',
        'bruno_d',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        '33333333-3333-3333-3333-333333333333',
        'carla',
        crypt('Carla12345!', gen_salt('bf')),
        'Carla',
        'Mendez',
        DATE '1998-01-05',
        'carla_m',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO publicaciones (
    id, usuario_id, mensaje, fecha_publicacion, created_at, updated_at, deleted_at
) VALUES
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        '11111111-1111-1111-1111-111111111111',
        'Hola, soy Ana',
        CURRENT_TIMESTAMP - INTERVAL '3 hour',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
        '22222222-2222-2222-2222-222222222222',
        'Primer post de Bruno',
        CURRENT_TIMESTAMP - INTERVAL '2 hour',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
        '33333333-3333-3333-3333-333333333333',
        'Post inicial de Carla',
        CURRENT_TIMESTAMP - INTERVAL '1 hour',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    )
ON CONFLICT (id) DO NOTHING;

