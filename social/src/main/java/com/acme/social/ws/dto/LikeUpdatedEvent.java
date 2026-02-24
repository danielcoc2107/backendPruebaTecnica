// src/main/java/com/acme/social/ws/dto/LikeUpdatedEvent.java
package com.acme.social.ws.dto;

import java.util.UUID;

public record LikeUpdatedEvent(UUID postId, boolean liked, long totalLikes) {}
