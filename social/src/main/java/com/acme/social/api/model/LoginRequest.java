// src/main/java/com/acme/social/api/model/LoginRequest.java
package com.acme.social.api.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
