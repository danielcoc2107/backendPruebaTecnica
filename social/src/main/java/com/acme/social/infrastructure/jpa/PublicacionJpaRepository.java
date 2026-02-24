package com.acme.social.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PublicacionJpaRepository extends JpaRepository<PublicacionEntity, UUID> {

    @Query("""
        SELECT p FROM PublicacionEntity p
        WHERE p.deletedAt IS NULL
        ORDER BY p.fechaPublicacion DESC
    """)
    List<PublicacionEntity> findAllActivas();
}
