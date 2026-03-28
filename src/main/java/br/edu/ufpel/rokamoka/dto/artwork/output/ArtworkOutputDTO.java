package br.edu.ufpel.rokamoka.dto.artwork.output;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Image;

import java.util.Base64;

public record ArtworkOutputDTO(
        Long id,
        String nome,
        String descricao,
        String nomeArtista,
        String qrCode,
        String link,
        String image) {

    public ArtworkOutputDTO(Artwork artwork) {
        this(
                artwork.getId(),
                artwork.getNome(),
                artwork.getDescricao(),
                artwork.getNomeArtista(),
                artwork.getQrCode(),
                artwork.getLink(),
                null);
    }

    public ArtworkOutputDTO(Artwork artwork, Image image) {
        this(
                artwork.getId(),
                artwork.getNome(),
                artwork.getDescricao(),
                artwork.getNomeArtista(),
                artwork.getQrCode(),
                artwork.getLink(),
                image == null ? null : Base64.getEncoder().encodeToString(image.getConteudo()));
    }
}
