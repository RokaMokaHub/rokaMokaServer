package br.edu.ufpel.rokamoka.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserBasicDTO(
        @NotNull @Email String email,
        @NotBlank String password,
        @NotBlank String name,
        String deviceId
) {}
