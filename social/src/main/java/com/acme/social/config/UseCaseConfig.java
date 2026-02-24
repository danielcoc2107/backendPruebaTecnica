// src/main/java/com/acme/social/config/UseCaseConfig.java
package com.acme.social.config;

import com.acme.social.application.ports.LikeCommandPort;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ToggleLikeUseCase toggleLikeUseCase(LikeCommandPort port) {
        return new ToggleLikeUseCase(port);
    }
}
