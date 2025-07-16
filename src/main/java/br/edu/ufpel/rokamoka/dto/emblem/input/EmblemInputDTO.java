package br.edu.ufpel.rokamoka.dto.emblem.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author MauricioMucci
 */
public record EmblemInputDTO(
        @NotBlank String nome,
        String descricao,
        @NotNull Long exhibitionId
){}
