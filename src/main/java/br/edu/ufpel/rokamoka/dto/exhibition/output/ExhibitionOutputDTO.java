package br.edu.ufpel.rokamoka.dto.exhibition.output;

import br.edu.ufpel.rokamoka.core.Exhibition;

/**
 * A Data Transfer Object (DTO) representing the output details of an exhibition.
 *
 * @param id The unique identifier of the exhibition.
 * @param name The name of the exhibition.
 * @param description The description of the exhibition.
 * @param numberOfArtworks The total number of artworks featured in the exhibition.
 * @param location The name of the place where the exhibition is held.
 *
 * @author MauricioMucci
 * @see Exhibition
 */
public record ExhibitionOutputDTO(Long id, String name, String description, Long numberOfArtworks, String location) {

    public ExhibitionOutputDTO(Exhibition exhibition) {
        this(
                exhibition.getId(),
                exhibition.getName(),
                exhibition.getDescription(),
                (long) exhibition.getArtworks().size(),
                exhibition.getLocation().getNome());
    }

    public ExhibitionOutputDTO(Exhibition exhibition, Long numberOfArtworks) {
        this(
                exhibition.getId(),
                exhibition.getName(),
                exhibition.getDescription(),
                numberOfArtworks,
                exhibition.getLocation().getNome());
    }
}
