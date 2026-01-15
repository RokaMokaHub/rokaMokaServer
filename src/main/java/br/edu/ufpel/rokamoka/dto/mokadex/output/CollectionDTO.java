package br.edu.ufpel.rokamoka.dto.mokadex.output;

import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;

import java.util.Set;

/**
 * A Data Transfer Object representing a set of artworks associated with a specific exhibition.
 *
 * @param exhibition The associated exhibition, encapsulated as an {@link ExhibitionOutputDTO}.
 * @param artwork The set of artworks, encapsulated as an {@link ArtworkOutputDTO}.
 *
 * @author mauri
 * @see ExhibitionOutputDTO
 * @see ArtworkOutputDTO
 * @see MokadexOutputDTO
 */
public record CollectionDTO(
        ExhibitionOutputDTO exhibition,
        Set<ArtworkOutputDTO> artwork
) {}
