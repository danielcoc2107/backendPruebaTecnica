// src/main/java/com/acme/social/api/model/CreatePostHttpRequest.java
package com.acme.social.api.model;

import java.util.UUID;

public record CreatePostHttpRequest(UUID userId, String message) {}
