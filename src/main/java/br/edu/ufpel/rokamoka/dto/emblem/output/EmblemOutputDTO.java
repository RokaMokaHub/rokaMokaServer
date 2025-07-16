package br.edu.ufpel.rokamoka.dto.emblem.output;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author MauricioMucci
 */
public record EmblemOutputDTO(
        @NotNull Long id,
        @NotBlank String nome,
        String descricao,
        @Valid ExhibitionOutputDTO exhibition
) {

    public EmblemOutputDTO(Emblem emblem) {
        this(
                emblem.getId(),
                emblem.getNome(),
                emblem.getDescricao(),
                new ExhibitionOutputDTO(emblem.getExhibition())
        );
    }
}
