// src/main/java/com/acme/social/api/model/LoginResponse.java
package com.acme.social.api.model;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds) {}
