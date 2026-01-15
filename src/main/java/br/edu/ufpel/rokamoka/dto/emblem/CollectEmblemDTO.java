package br.edu.ufpel.rokamoka.dto.emblem;

import jakarta.validation.constraints.NotNull;

/**
 * A Data Transfer Object (DTO) for supporting emblem collecting.
 *
 * @param mokadexId The unique identifier for the mokadex.
 * @param exhibitionId The unique identifier for the exhibition.
 *
 * @author MauricioMucci
 */
public record CollectEmblemDTO(@NotNull Long mokadexId, @NotNull Long exhibitionId) {}
