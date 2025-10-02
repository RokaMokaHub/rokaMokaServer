package br.edu.ufpel.rokamoka.dto.location.input;

import br.edu.ufpel.rokamoka.dto.GroupValidators;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * A Data Transfer Object (DTO) representing the input data of a location.
 *
 * @param id The unique identifier of the location.
 * @param nome The unique name of the location.
 * @param endereco The address details of the location, encapsulated within an {@code AddressInputDTO}.
 *
 * @author MauricioMucci
 * @see br.edu.ufpel.rokamoka.core.Location
 * @see AddressInputDTO
 */
public record LocationInputDTO(
        @NotNull(groups = GroupValidators.Update.class) @Null(groups = GroupValidators.Create.class) Long id,
        @NotBlank String nome,
        @NotNull(groups = GroupValidators.Create.class) @Valid AddressInputDTO endereco
) {}
