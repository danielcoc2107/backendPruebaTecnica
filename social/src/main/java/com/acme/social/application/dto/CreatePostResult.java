// src/main/java/com/acme/social/application/dto/CreatePostResult.java
package com.acme.social.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreatePostResult(UUID postId, OffsetDateTime publishedAt) {}
