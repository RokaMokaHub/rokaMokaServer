package br.edu.ufpel.rokamoka.dto.artwork.output;

import br.edu.ufpel.rokamoka.core.Artwork;

public record ArtworkOutputDTO(Long id, String nome) {

    public ArtworkOutputDTO(Artwork artwork) {
        this(artwork.getId(), artwork.getNome());
    }
}
