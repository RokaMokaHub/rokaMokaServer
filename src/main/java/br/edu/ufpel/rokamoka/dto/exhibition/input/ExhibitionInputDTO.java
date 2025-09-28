package br.edu.ufpel.rokamoka.dto.exhibition.input;

import br.edu.ufpel.rokamoka.dto.location.input.AddressInputDTO;

public record ExhibitionInputDTO(String name, String description,
                                 AddressInputDTO addressInputDTO) {
}
