package br.edu.ufpel.rokamoka.dto.artwork.input;

import br.edu.ufpel.rokamoka.dto.GroupValidators.Create;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.web.multipart.MultipartFile;

public record ArtworkInputDTO(
        @Null(groups = Create.class) @NotNull(groups = Update.class) Long id,
        @NotBlank String nome,
        String nomeArtista,
        String descricao,
        String link,
        String qrCode,
        MultipartFile image) {}
