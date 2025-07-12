package br.edu.ufpel.rokamoka.dto.artwork.input;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record ArtworkInputDTO(@NotBlank String nome,
                              String nomeArtista,
                              String descricao,
                              String link,
                              String qrCode,
                              MultipartFile image) {
}
