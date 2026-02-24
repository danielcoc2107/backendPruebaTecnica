// src/main/java/com/acme/social/api/model/ToggleLikeHttpResponse.java
package com.acme.social.api.model;

public record ToggleLikeHttpResponse(boolean liked, long totalLikes) {}
