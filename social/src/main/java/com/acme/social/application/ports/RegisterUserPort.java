package com.acme.social.application.ports;

import com.acme.social.application.dto.RegisterResult;

import java.time.LocalDate;

public interface RegisterUserPort {

    RegisterResult register(
            String username,
            String passwordHash,
            String nombres,
            String apellidos,
            LocalDate fechaNacimiento,
            String alias
    );
}
