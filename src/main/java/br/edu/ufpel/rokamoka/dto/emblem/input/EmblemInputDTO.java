package br.edu.ufpel.rokamoka.dto.emblem.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for encapsulating the input data of an {@code Emblem} entity.
 *
 * @param nome       The name of the {@code Emblem}.
 * @param descricao  A brief description of the {@code Emblem}.
 * @param exhibitionId The ID of the associated exhibition.

 *
 * @author MauricioMucci
 * @see br.edu.ufpel.rokamoka.core.Emblem
 */
public record EmblemInputDTO(
        @NotBlank String nome,
        String descricao,
        @NotNull Long exhibitionId
){}
