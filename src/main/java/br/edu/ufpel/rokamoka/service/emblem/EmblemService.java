package br.edu.ufpel.rokamoka.service.emblem;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaNoUserInContextException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final MokadexRepository mokadexRepository;
    private final IArtworkService artworkService;
    private final ArtworkRepository artworkRepository;
    private final IExhibitionService exhibitionService;

    @Override
    public Emblem findById(Long emblemId) {
        return this.emblemRepository.findById(emblemId).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public EmblemOutputDTO findByIdWithArtworks(Long emblemId) {
        Emblem emblem = this.findById(emblemId);
        this.assertLoggedUserHasEmblem(emblem);

        Set<Long> artworkIds = this.artworkService.getAllArtworkByExhibitionId(emblem.getExhibition().getId())
                .stream()
                .map(Artwork::getId)
                .collect(Collectors.toSet());

        if (artworkIds.isEmpty()) {
            return new EmblemOutputDTO(emblem);
        }

        var artworks = this.artworkRepository.createFullArtworkInfo(artworkIds).stream().toList();

        return new EmblemOutputDTO(emblem, artworks);
    }

    @Override
    public Optional<Optional<Emblem>> findByExhibitionId(Long exhibitionId) {
        Optional<Emblem> maybeEmblem = this.emblemRepository.findEmblemByExhibitionId(exhibitionId);
        maybeEmblem.ifPresentOrElse(emblem -> log.info("[{}] encontrado com sucesso", emblem),
                () -> log.info("[{}] não foi encontrado", Emblem.class.getSimpleName()));
        return maybeEmblem;
    }


    @Override
    public Emblem create(EmblemInputDTO emblemInputDTO) {
        log.info("Criando [{}] para o [{}]", Emblem.class.getSimpleName(), emblemInputDTO);

        var emblem = new Emblem();
        BeanUtils.copyProperties(emblemInputDTO, emblem);
        emblem.setExhibition(this.exhibitionService.getExhibitionOrElseThrow(emblemInputDTO.exhibitionId()));
        emblem = this.emblemRepository.save(emblem);

        log.info("Novo [{}] criado com sucesso", emblem);
        return emblem;
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public Emblem delete(@NotNull Long emblemId) {
        Emblem emblem = this.emblemRepository.findById(emblemId).orElseThrow(RokaMokaContentNotFoundException::new);
        this.emblemRepository.delete(emblem);
        return emblem;
    }

    @Override
    public boolean existsEmblemByExhibitionId(Long exhibitionId) {
        return this.emblemRepository.existsEmblemByExhibitionId(exhibitionId);
    }

    private void assertLoggedUserHasEmblem(Emblem emblem) {
        String userName;
        try {
            userName = ServiceContext.getContext().getUsernameOrThrow();
        } catch (RokaMokaNoUserInContextException e) {
            log.error(e.getMessage());
            throw new RokaMokaForbiddenException("Usuário não possui mokadex");
        }
        Mokadex mokadex = this.mokadexRepository.findMokadexByUsername(userName)
                .orElseThrow(() -> new RokaMokaForbiddenException("Usuário não possui mokadex"));

        if (!mokadex.containsEmblem(emblem)) {
            throw new RokaMokaForbiddenException("Usuário não possui o emblema solicitado");
        }
    }

}
