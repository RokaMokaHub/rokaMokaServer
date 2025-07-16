package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.config.broker.RabbitMQExchangeConfigProperties;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
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
 * Service implementation of the {@link IMokadexService} interface for managing operations on the {@link Mokadex}
 * resource.
 *
 * @author MauricioMucci
 * @see MokadexRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MokadexService implements IMokadexService {

    private final MokadexRepository mokadexRepository;
    private final EmblemRepository emblemRepository;
    private final IArtworkService artworkService;

    private final RabbitMQExchangeConfigProperties exchangeConfigProperties;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Retrieves an existing {@link Mokadex} associated with the given user or creates a new one if none exists.
     *
     * @param user The {@link User} for whom the {@code Mokadex} is to be retrieved or created.
     *
     * @return If no {@code Mokadex} exists, a newly created {@code Mokadex} is returned. Otherwise, returns
     * {@code Mokadex} associated with the specified {@code User}.
     * @throws ServiceException if the {@code user} is {@code null}.
     * @see MokadexService#getMokadexByUser(User)
     * @see MokadexService#createMokadexByUser(User)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mokadex getOrCreateMokadexByUser(User user) {
        if (user == null) {
            throw new ServiceException("O usuário não pode ser nulo ao tentar criar ou recuperar o Mokadex");
        }
        return getMokadexByUser(user).orElseGet(() -> createMokadexByUser(user));
    }

    /**
     * Retrieves an {@link Optional} containing the {@link Mokadex} associated with the specified {@link User}.
     *
     * @param usuario The {@code User} whose associated {@link Mokadex} is to be retrieved.
     *
     * @return An {@code Optional} containing the {@code Mokadex} if found, or empty if no matching {@code Mokadex}
     * exists.
     * @see MokadexRepository#findMokadexByUsername(String)
     */
    private Optional<Mokadex> getMokadexByUser(User usuario) {
        log.info("Buscando pelo [{}] do [{}]", Mokadex.class.getSimpleName(), usuario);

        Optional<Mokadex> maybeMokadex = this.mokadexRepository.findMokadexByUsername(usuario.getNome());
        maybeMokadex.ifPresentOrElse(
                mokadex -> log.info("[{}] foi encontrado", mokadex),
                () -> log.info("Não foi encontrado nenhum [{}] para o [{}] informado",
                        Mokadex.class.getSimpleName(), usuario)
        );

        return maybeMokadex;
    }

    /**
     * Creates a new {@link Mokadex} instance for the specified {@link User} and persists it in the repository.
     *
     * @param user The {@code User} for whom the new {@code Mokadex} is to be created.
     *
     * @return The newly created and persisted {@code Mokadex} instance associated with the given {@code user}.
     */
    private Mokadex createMokadexByUser(User user) {
        log.info("Criando [{}] para o [{}]", Mokadex.class.getSimpleName(), user);

        var mokadex = new Mokadex();
        mokadex.setUsuario(user);
        mokadex = this.mokadexRepository.save(mokadex);

        log.info("Novo [{}] criado com sucesso para o [{}] informado", mokadex, user);
        return mokadex;
    }

    @Override
    public MokadexOutputDTO buildMokadexOutputDTOByMokadex(Mokadex mokadex) {
        log.info("Construindo {} para o {} informado", MokadexOutputDTO.class.getSimpleName(), mokadex);

        Set<CollectionDTO> collectionDTOSet = buildCollectionSetByMokadex(mokadex);
        Set<EmblemOutputDTO> emblemDTOSet = buildEmblemSetByMokadex(mokadex);

        return new MokadexOutputDTO(collectionDTOSet, emblemDTOSet);
    }

    private Set<CollectionDTO> buildCollectionSetByMokadex(Mokadex mokadex) {
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

        return collectionDTOSet;
    }

    private Set<EmblemOutputDTO> buildEmblemSetByMokadex(Mokadex mokadex) {
        return emblemRepository.findEmblemsByMokadexId(mokadex.getId())
                .stream()
                .map(EmblemOutputDTO::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public MokadexOutputDTO collectStar(@NotBlank String qrCode)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        Mokadex mokadex = getMokadexByLoggedUser();
        Artwork artwork = this.artworkService.getByQrCodeOrThrow(qrCode);

        if (mokadex.containsArtwork(artwork)) {
            throw new RokaMokaContentDuplicatedException("Obra já foi coletada");
        }
        if (!mokadex.addArtwork(artwork)) {
            throw new ServiceException("Erro ao coletar estrela na mokadex");
        }

        log.info("Estrela coletada com sucesso!");
        mokadex = this.mokadexRepository.save(mokadex);
        sendMessageToBrokerIfReady(mokadex, artwork);

        return this.buildMokadexOutputDTOByMokadex(mokadex);
    }

    private Mokadex getMokadexByLoggedUser() throws RokaMokaContentNotFoundException {
        UserAuthenticated loggedInUser = ServiceContext.getContext().getUser();
        return this.mokadexRepository
                .findMokadexByUsername(loggedInUser.getUsername())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Mokadex não encontrado para usuário logado"));
    }

    private void sendMessageToBrokerIfReady(Mokadex mokadex, Artwork artwork) {
        CollectEmblemDTO collectEmblemDTO = new CollectEmblemDTO(mokadex.getId(), artwork.getId());

        if (!emblemRepository.existsEmblemByArtworkId(artwork.getId())) {
            throw new ServiceException("Emblema não foi criado");
        }

        if (emblemRepository.hasCollectedAllArtworksInExhibition(mokadex.getId(), artwork.getId())) {
            this.rabbitTemplate.convertAndSend(this.exchangeConfigProperties.getEmblems(),
                    "",
                    collectEmblemDTO);
        }
    }
}
