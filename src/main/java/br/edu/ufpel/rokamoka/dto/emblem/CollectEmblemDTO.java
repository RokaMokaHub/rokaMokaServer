package br.edu.ufpel.rokamoka.dto.emblem;

import jakarta.validation.constraints.NotNull;

/**
 * @author MauricioMucci
 */
public record CollectEmblemDTO(@NotNull Long mokadexId, @NotNull Long newlyAddedArtworkId) {}
