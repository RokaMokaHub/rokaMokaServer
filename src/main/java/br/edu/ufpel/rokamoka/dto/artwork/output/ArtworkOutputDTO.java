package br.edu.ufpel.rokamoka.dto.artwork.output;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public record ArtworkOutputDTO(Long id, String nome, String descricao, String nomeArtista, String image) {

    public ArtworkOutputDTO(Artwork artwork) {
        this(artwork.getId(), artwork.getNome(), artwork.getDescricao(), artwork.getNomeArtista(), null);
    }

    public ArtworkOutputDTO(Artwork artwork, Image image) {
        this(artwork.getId(), artwork.getNome(), artwork.getDescricao(), artwork.getNomeArtista(), image == null ? null : Base64.getEncoder().encodeToString(image.getConteudo()));

    }

    public ArtworkOutputDTO(Artwork artwork, MultipartFile image) throws IOException {
        this(artwork.getId(), artwork.getNome(), artwork.getDescricao(), artwork.getNomeArtista(), Base64.getEncoder().encodeToString(image.getBytes()));

    }
}
