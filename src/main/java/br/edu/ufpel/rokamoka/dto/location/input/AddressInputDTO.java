package br.edu.ufpel.rokamoka.dto.location.input;

import br.edu.ufpel.rokamoka.utils.location.CepConstraint;
import jakarta.validation.constraints.NotBlank;

public record AddressInputDTO(
        @NotBlank(message = "A rua é obrigatória") String rua,
        @NotBlank(message = "O número é obrigatório") String numero,
        @CepConstraint String cep,
        String complemento
) {
}
