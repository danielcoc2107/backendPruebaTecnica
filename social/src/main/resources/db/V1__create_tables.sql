CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    fecha_nacimiento DATE,
    alias VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE publicaciones (
    id UUID PRIMARY KEY,
    usuario_id UUID REFERENCES usuarios(id),
    mensaje TEXT,
    fecha_publicacion TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE likes (
    id UUID PRIMARY KEY,
    usuario_id UUID REFERENCES usuarios(id),
    publicacion_id UUID REFERENCES publicaciones(id),
    created_at TIMESTAMP
);
