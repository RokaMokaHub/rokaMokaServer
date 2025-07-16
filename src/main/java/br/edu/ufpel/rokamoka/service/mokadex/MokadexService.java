package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation of the {@link IMokadexService} interface for managing Mokadex-related operations.
 *
 * @author mauri
 * @see MokadexRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MokadexService implements IMokadexService {

    private final MokadexRepository mokadexRepository;
    private final IArtworkService artworkService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mokadex getOrCreateMokadexByUser(@Valid User usuario){
        return getMokadexByUser(usuario)
                .orElseGet(() -> createMokadexByUser(usuario));
    }

    private Optional<Mokadex> getMokadexByUser(User usuario) {
        log.info("Buscando pelo [{}] do [{}]", Mokadex.class.getSimpleName(), usuario);

        Optional<Mokadex> maybeMokadex = mokadexRepository.findMokadexByUsername(usuario.getNome());
        maybeMokadex.ifPresentOrElse(
                mokadex -> log.info("[{}] foi encontrado", mokadex),
                () -> log.info("Não foi encontrado nenhum [{}] para o [{}] informado", Mokadex.class.getSimpleName(), usuario)
        );

        return maybeMokadex;
    }

    private Mokadex createMokadexByUser(User usuario) {
        log.info("Criando [{}] para o [{}]", Mokadex.class.getSimpleName(), usuario);

        var mokadex = new Mokadex();
        mokadex.setUsuario(usuario);
        mokadex = mokadexRepository.save(mokadex);

        log.info("Novo [{}] criado com sucesso para o [{}] informado", mokadex, usuario);
        return mokadex;
    }

    @Override
    public MokadexOutputDTO buildMokadexOutputDTOByMokadex(Mokadex mokadex) {
        log.info("Construindo {} para o {} informado", MokadexOutputDTO.class.getSimpleName(), mokadex);

        Set<Artwork> artworkSet = mokadex.getArtworks();
        Set<Exhibition> exhibitionSet = artworkSet.stream()
                .map(Artwork::getExhibition)
                .collect(Collectors.toUnmodifiableSet());

        Set<CollectionDTO> collectionDTOSet = new HashSet<>();
        exhibitionSet.forEach(exhibition -> {
            ExhibitionOutputDTO exhibitionDTO = new ExhibitionOutputDTO(exhibition);
            Set<ArtworkOutputDTO> artworkDTOSet = artworkSet.stream()
                    .filter(artwork -> artwork.getExhibition().equals(exhibition))
                    .map(ArtworkOutputDTO::new)
                    .collect(Collectors.toUnmodifiableSet());
            collectionDTOSet.add(new CollectionDTO(exhibitionDTO, artworkDTOSet));
        });

        MokadexOutputDTO resultDTO = new MokadexOutputDTO(collectionDTOSet);
        log.info("Retornando {} para o {} informado", resultDTO, mokadex);
        return resultDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public MokadexOutputDTO collectStar(@NotBlank String qrCode) throws RokaMokaContentNotFoundException {
        Mokadex mokadex = getMokadexByLoggedUser();
        Artwork artwork = artworkService.getByQrCodeOrThrow(qrCode);

        if (!mokadex.addArtwork(artwork)) {
            throw new ServiceException("Erro ao coletar estrela na mokadex");
        }

        log.info("Estrela coletada com sucesso!");
        mokadex = mokadexRepository.save(mokadex);
        CollectEmblemDTO collectEmblemDTO = new CollectEmblemDTO(mokadex.getId(), artwork.getId());
        rabbitTemplate.convertAndSend("emblems.v1.collect", collectEmblemDTO);

        return this.buildMokadexOutputDTOByMokadex(mokadex);
    }

    private Mokadex getMokadexByLoggedUser() throws RokaMokaContentNotFoundException {
        UserAuthenticated loggedInUser = ServiceContext.getContext().getUser();
        return mokadexRepository.findMokadexByUsername(loggedInUser.getUsername())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Mokadex não encontrado para usuário logado"));
    }
}
