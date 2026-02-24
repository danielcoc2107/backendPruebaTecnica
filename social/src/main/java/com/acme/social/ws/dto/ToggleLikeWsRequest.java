// src/main/java/com/acme/social/ws/dto/ToggleLikeWsRequest.java
package com.acme.social.ws.dto;

import java.util.UUID;

public record ToggleLikeWsRequest(UUID postId) {}
