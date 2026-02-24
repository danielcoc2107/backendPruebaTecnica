package com.acme.social.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, UUID> {

    long countByPublicacionId(UUID publicacionId);

    boolean existsByPublicacionIdAndUsuarioId(UUID publicacionId, UUID usuarioId);
}
