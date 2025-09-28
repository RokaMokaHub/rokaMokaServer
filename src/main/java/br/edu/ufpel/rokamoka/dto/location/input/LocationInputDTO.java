package br.edu.ufpel.rokamoka.dto.location.input;

import br.edu.ufpel.rokamoka.dto.GroupValidators;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author MauricioMucci
 */
public record LocationInputDTO(
        @NotNull(groups = GroupValidators.Create.class) Long id,
        @NotBlank String name,
        @NotNull(groups = GroupValidators.Create.class) @Valid AddressInputDTO endereco
) {}
