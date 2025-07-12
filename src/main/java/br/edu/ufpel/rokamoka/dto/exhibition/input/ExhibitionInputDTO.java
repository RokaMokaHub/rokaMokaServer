package br.edu.ufpel.rokamoka.dto.exhibition.input;

import br.edu.ufpel.rokamoka.dto.address.input.EnderecoDTO;

public record ExhibitionInputDTO(String name, String description,
                                 EnderecoDTO enderecoDTO) {
}
