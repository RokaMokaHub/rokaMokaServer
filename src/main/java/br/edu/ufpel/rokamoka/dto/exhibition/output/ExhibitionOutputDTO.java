package br.edu.ufpel.rokamoka.dto.exhibition.output;

import br.edu.ufpel.rokamoka.core.Exhibition;

public record ExhibitionOutputDTO(Long id, String name, String description) {
    public ExhibitionOutputDTO(Exhibition exhibition) {
        this(exhibition.getId(), exhibition.getName(), exhibition.getDescription());
    }
}
