package br.edu.ufpel.rokamoka.dto.exhibition.input;

import br.edu.ufpel.rokamoka.dto.GroupValidators.Create;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * A Data Transfer Object (DTO) representing the input data of a exhibition.
 *
 * @param id The unique identifier of the exhibition.
 * @param name The name of the exhibition.
 * @param description The description of the exhibition.
 * @param locationId The unique identifier of the place where the exhibition is held.
 *
 * @author MauricioMucci
 * @see br.edu.ufpel.rokamoka.core.Exhibition
 */
public record ExhibitionInputDTO(
        @NotNull(groups = Update.class, message = "O ID da exposição é obrigatório durante a atualização") @Null(
                groups = Create.class, message = "O ID da exposição não pode ser fornecido durante a criação") Long id,
        @NotBlank(message = "O nome da exposição é obrigatório") String name,
        String description,
        @NotNull(groups = Create.class, message = "O ID do local é obrigatório durante a criação") Long locationId) {}
