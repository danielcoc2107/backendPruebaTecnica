// src/main/java/com/acme/social/application/dto/CreatePostRequest.java
package com.acme.social.application.dto;

import java.util.UUID;

public record CreatePostRequest(UUID userId, String message) {}
