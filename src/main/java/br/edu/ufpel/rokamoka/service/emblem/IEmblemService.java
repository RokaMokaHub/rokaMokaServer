package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
public interface IEmblemService {

    Emblem findById(Long emblemId) throws RokaMokaContentNotFoundException;

    Optional<Emblem> findByExhibitionId(Long exhibitionId);

    Emblem create(@Valid EmblemInputDTO emblemInputDTO) throws RokaMokaContentNotFoundException;

    Emblem delete(@NotNull Long emblemId) throws RokaMokaContentNotFoundException;
}
