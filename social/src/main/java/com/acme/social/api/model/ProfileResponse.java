package com.acme.social.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Perfil del usuario autenticado")
public class ProfileResponse {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String alias;
    private LocalDate fechaNacimiento;
}
