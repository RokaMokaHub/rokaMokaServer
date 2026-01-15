package br.edu.ufpel.rokamoka.dto.mokadex.output;

import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;

import java.util.Set;

/**
 * A Data Transfer Object (DTO) representing the output data of a user's mokadex.
 *
 * @param collectionSet The set of {@link CollectionDTO} objects representing the relation between the collected
 * artworks and their respective exhibition.
 * @param emblemSet The set of {@link EmblemOutputDTO} objects representing emblems within the mokadex.
 *
 * @author MauricioMucci
 * @see br.edu.ufpel.rokamoka.core.Mokadex
 */
public record MokadexOutputDTO(
        Set<CollectionDTO> collectionSet,
        Set<EmblemOutputDTO> emblemSet
) {}
