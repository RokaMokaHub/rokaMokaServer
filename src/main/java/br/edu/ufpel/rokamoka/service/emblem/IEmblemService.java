package br.edu.ufpel.rokamoka.service.emblem;

import java.util.Optional;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface for managing and retrieving information related to {@link Emblem}.
 *
 * @author MauricioMucci
 * @see EmblemService
 */
public interface IEmblemService {

    Emblem findById(Long emblemId);

    EmblemOutputDTO findByIdWithArtworks(Long emblemId);

    Optional<Emblem> findByExhibitionId(Long exhibitionId);

    Emblem create(@Valid EmblemInputDTO emblemInputDTO);

    Emblem delete(@NotNull Long emblemId);

    boolean existsEmblemByExhibitionId(Long exhibitionId);
}
