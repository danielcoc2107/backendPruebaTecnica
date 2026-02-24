// src/main/java/com/acme/social/api/model/ToggleLikeHttpRequest.java
package com.acme.social.api.model;

import java.util.UUID;

public record ToggleLikeHttpRequest(UUID userId, UUID postId) {}
