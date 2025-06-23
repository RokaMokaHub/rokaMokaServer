package br.edu.ufpel.rokamoka.dto.mokadex.output;

import br.edu.ufpel.rokamoka.core.Mokadex;

import java.util.Set;

/**
 * A Data Transfer Object representing the output data of a user's mokadex.
 *
 * @param collectionSet The set of {@link CollectionDTO} objects representing collections within the mokadex.
 *
 * @author mauri
 * @see Mokadex
 * @see CollectionDTO
 */
public record MokadexOutputDTO(Set<CollectionDTO> collectionSet) {}
