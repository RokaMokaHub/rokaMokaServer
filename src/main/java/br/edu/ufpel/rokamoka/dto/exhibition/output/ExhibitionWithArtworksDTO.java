package br.edu.ufpel.rokamoka.dto.exhibition.output;

import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;

import java.util.List;

public record ExhibitionWithArtworksDTO(String exhibitionName, List<ArtworkOutputDTO> artworkOutputDTOS) {
}
