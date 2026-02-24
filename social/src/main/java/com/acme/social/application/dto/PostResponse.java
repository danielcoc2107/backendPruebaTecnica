package com.acme.social.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String mensaje;
    private String autorAlias;
    private LocalDateTime fechaPublicacion;
    private long totalLikes;
    private boolean likedByMe;
}
