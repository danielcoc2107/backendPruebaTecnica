// src/main/java/com/acme/social/api/model/CreatePostHttpResponse.java
package com.acme.social.api.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreatePostHttpResponse(UUID postId, OffsetDateTime publishedAt) {}
