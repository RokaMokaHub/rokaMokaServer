package br.edu.ufpel.rokamoka.dto.exhibition.input;

import br.edu.ufpel.rokamoka.dto.address.input.EnderecoDTO;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;

import java.util.List;

public record ExhibitionInputDTO(String name, String description, List<ArtworkInputDTO> artworks,
                                 EnderecoDTO enderecoDTO) {
}
