package br.edu.ufpel.rokamoka.dto.mokadex.output;

import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;

import java.util.Set;

/**
 * @author mauri
 */
public record CollectionDTO(
        ExhibitionOutputDTO exhibition,
        Set<ArtworkOutputDTO> artwork
) {}
