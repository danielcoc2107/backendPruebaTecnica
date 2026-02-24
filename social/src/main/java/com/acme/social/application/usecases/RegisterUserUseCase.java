package com.acme.social.application.usecases;

import com.acme.social.application.dto.RegisterRequest;
import com.acme.social.application.dto.RegisterResult;
import com.acme.social.application.ports.RegisterUserPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

public final class RegisterUserUseCase {

    private final RegisterUserPort registerUserPort;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterUserUseCase(RegisterUserPort registerUserPort) {
        this.registerUserPort = Objects.requireNonNull(registerUserPort);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public RegisterResult execute(RegisterRequest req) {
        Objects.requireNonNull(req, "request no puede ser null");

        String username = req.username().trim();
        String passwordHash = passwordEncoder.encode(req.password());
        String nombres = req.nombres().trim();
        String apellidos = req.apellidos().trim();
        String alias = req.alias().trim();

        return registerUserPort.register(
                username,
                passwordHash,
                nombres,
                apellidos,
                req.fechaNacimiento(),
                alias
        );
    }
}
