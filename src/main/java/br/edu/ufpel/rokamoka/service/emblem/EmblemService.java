package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EmblemService implements IEmblemService {

    private final EmblemRepository emblemRepository;
    private final MokadexRepository mokadexRepository;
    private final ArtworkRepository artworkRepository;
    private final ExhibitionService exhibitionService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Emblem> collectEmblem(@Valid CollectEmblemDTO collectEmblemDTO) {
        Artwork artwork = getArtworkById(collectEmblemDTO.artworkId());
        Optional<Emblem> maybeEmblem = emblemRepository.findEmblemByArtworkId(artwork.getId());
        if (maybeEmblem.isEmpty()) {
            log.warn("[{}] não encontrado usando [{}]", Emblem.class.getSimpleName(), artwork);
            return Optional.empty();
        }

        Mokadex mokadex = getMokadexById(collectEmblemDTO.mokadexId());
        Emblem emblem = maybeEmblem.get();
        if (mokadex.containsEmblem(emblem)) {
            throw new AmqpRejectAndDontRequeueException("Emblema já foi coletada");
        }
        if (!mokadex.addEmblem(emblem)) {
            throw new AmqpRejectAndDontRequeueException("Erro ao coletar emblema na mokadex");
        }
        log.info("Emblema coletado com sucesso!");
        mokadexRepository.save(mokadex);

        return Optional.of(emblem);
    }

    private Mokadex getMokadexById(Long mokadexId) {
        return mokadexRepository.findById(mokadexId)
                .orElseThrow(() ->
                        new ServiceException("Erro ao buscar pelo MOKADEX com id: " + mokadexId));
    }

    private Artwork getArtworkById(Long artworkId) {
        return artworkRepository.findById(artworkId)
                .orElseThrow(() ->
                        new ServiceException("Erro ao buscar pela OBRA com id: " + artworkId));
    }

    @Override
    public Emblem create(EmblemInputDTO emblemInputDTO) throws RokaMokaContentNotFoundException {
        log.info("Criando [{}] para o [{}]", Emblem.class.getSimpleName(), emblemInputDTO);

        var emblem = new Emblem();
        BeanUtils.copyProperties(emblemInputDTO, emblem);
        emblem.setExhibition(exhibitionService.findById(emblemInputDTO.exhibitionId()));
        emblem = emblemRepository.save(emblem);

        log.info("Novo [{}] criado com sucesso", emblem);
        return emblem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Emblem delete(@NotNull Long emblemId) throws RokaMokaContentNotFoundException {
        Emblem emblem = this.emblemRepository
                .findById(emblemId)
                .orElseThrow(RokaMokaContentNotFoundException::new);
        this.emblemRepository.delete(emblem);
        return emblem;
    }
}
