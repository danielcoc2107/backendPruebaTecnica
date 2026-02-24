package com.acme.social.application.usecases;

import com.acme.social.application.dto.PostResponse;
import com.acme.social.infrastructure.jpa.LikeJpaRepository;
import com.acme.social.infrastructure.jpa.PublicacionJpaRepository;
import com.acme.social.infrastructure.jpa.UsuarioEntity;
import com.acme.social.infrastructure.jpa.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListPostsUseCase {

    private final PublicacionJpaRepository postRepo;
    private final UsuarioJpaRepository userRepo;
    private final LikeJpaRepository likeRepo;

    public List<PostResponse> execute(UUID currentUserId) {
        return postRepo.findAllActivas()
                .stream()
                .map(p -> {
                    UsuarioEntity autor = userRepo.findById(p.getUsuarioId()).orElseThrow();
                    long totalLikes = likeRepo.countByPublicacionId(p.getId());
                    boolean likedByMe = likeRepo.existsByPublicacionIdAndUsuarioId(p.getId(), currentUserId);

                    return new PostResponse(
                            p.getId(),
                            p.getMensaje(),
                            autor.getAlias(),
                            p.getFechaPublicacion(),
                            totalLikes,
                            likedByMe
                    );
                })
                .toList();
    }
}
