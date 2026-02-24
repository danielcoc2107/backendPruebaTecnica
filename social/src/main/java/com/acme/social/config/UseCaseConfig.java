// src/main/java/com/acme/social/config/UseCaseConfig.java
package com.acme.social.config;

import com.acme.social.application.ports.LikeCommandPort;
import com.acme.social.application.ports.PostCommandPort;
import com.acme.social.application.ports.RegisterUserPort;
import com.acme.social.application.usecases.CreatePostUseCase;
import com.acme.social.application.usecases.RegisterUserUseCase;
import com.acme.social.application.usecases.ToggleLikeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreatePostUseCase createPostUseCase(PostCommandPort port) {
        return new CreatePostUseCase(port);
    }

    @Bean
    public ToggleLikeUseCase toggleLikeUseCase(LikeCommandPort port) {
        return new ToggleLikeUseCase(port);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(RegisterUserPort port) {
        return new RegisterUserUseCase(port);
    }
}
