package br.edu.ufpel.rokamoka.dto.location.input;

import jakarta.validation.constraints.NotBlank;

public record AddressInputDTO(
        @NotBlank String rua,
        @NotBlank String numero,
        @NotBlank String cep,
        String complemento
) {
}
