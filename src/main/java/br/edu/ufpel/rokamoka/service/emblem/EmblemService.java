package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Service implementation of the {@link IEmblemService} interface for managing operations on the {@link Emblem}
 * resource.
 *
 * @author MauricioMucci
 * @see EmblemRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EmblemService implements IEmblemService {

    private final EmblemRepository emblemRepository;
    private final IExhibitionService exhibitionService;

    @Override
    public Emblem findById(Long exhibitionId) throws RokaMokaContentNotFoundException {
        return this.emblemRepository.findById(exhibitionId).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Optional<Emblem> findByExhibitionId(Long exhibitionId) {
        Optional<Emblem> maybeEmblem = this.emblemRepository.findEmblemByExhibitionId(exhibitionId);
        maybeEmblem.ifPresentOrElse(
                emblem -> log.info("[{}] encontrado com sucesso", emblem),
                () -> log.info("[{}] não foi encontrado", Emblem.class.getSimpleName())
        );
        return maybeEmblem;
    }

    @Override
    public Emblem create(EmblemInputDTO emblemInputDTO) throws RokaMokaContentNotFoundException {
        log.info("Criando [{}] para o [{}]", Emblem.class.getSimpleName(), emblemInputDTO);

        var emblem = new Emblem();
        BeanUtils.copyProperties(emblemInputDTO, emblem);
        emblem.setExhibition(this.exhibitionService.findById(emblemInputDTO.exhibitionId()));
        emblem = this.emblemRepository.save(emblem);

        log.info("Novo [{}] criado com sucesso", emblem);
        return emblem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Emblem delete(@NotNull Long emblemId) throws RokaMokaContentNotFoundException {
        Emblem emblem = this.emblemRepository.findById(emblemId).orElseThrow(RokaMokaContentNotFoundException::new);
        this.emblemRepository.delete(emblem);
        return emblem;
    }

    @Override
    public boolean existsEmblemByExhibitionId(Long exhibitionId) {
        return this.emblemRepository.existsEmblemByExhibitionId(exhibitionId);
    }
}
