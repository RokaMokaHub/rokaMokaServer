package br.edu.ufpel.rokamoka.dto.emblem.output;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for encapsulating the output data of an {@code Emblem} entity.
 *
 * @param id         The unique identifier of the {@code Emblem}.
 * @param nome       The name of the {@code Emblem}.
 * @param descricao  A brief description of the {@code Emblem}.
 * @param exhibition The associated {@link ExhibitionOutputDTO}.
 *
 * @author MauricioMucci
 * @see br.edu.ufpel.rokamoka.core.Emblem
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
