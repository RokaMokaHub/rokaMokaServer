package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.config.broker.RabbitMQExchangeConfigProperties;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import br.edu.ufpel.rokamoka.utils.mokadex.MokadexCollectionsBuilder;
import br.edu.ufpel.rokamoka.utils.mokadex.MokadexEmblemsBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;

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

    private final IEmblemService emblemService;
    private final IArtworkService artworkService;
    private final ExhibitionService exhibitionService;

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQExchangeConfigProperties exchangeConfigProperties;

    @Override
    public Mokadex findById(@NotNull Long mokadexId) throws RokaMokaContentNotFoundException {
        return this.mokadexRepository.findById(mokadexId).orElseThrow(RokaMokaContentNotFoundException::new);
    }

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

    /**
     * Returns a {@link MokadexOutputDTO} based on the given {@link Mokadex}.
     *
     * @param mokadex The {@link Mokadex} object from which collections and emblems are derived.
     * @return A {@link MokadexOutputDTO} containing the processed collections and emblems.
     * @see MokadexCollectionsBuilder
     * @see MokadexEmblemsBuilder
     */
    @Override
    public MokadexOutputDTO getMokadexOutputDTOByMokadex(Mokadex mokadex) {
        log.info("Construindo {} para o {} informado", MokadexOutputDTO.class.getSimpleName(), mokadex);

        Set<CollectionDTO> collectionDTOSet = new MokadexCollectionsBuilder(mokadex).buildCollectionSet();
        Set<EmblemOutputDTO> emblemDTOSet = new MokadexEmblemsBuilder(mokadex).buildEmblemSet();

        return new MokadexOutputDTO(collectionDTOSet, emblemDTOSet);
    }

    /**
     * Tries to collect a star represented by the QR code and associates it with the current user's Mokadex.
     *
     * @param qrCode The QR code of the artwork to be collected. Must not be blank.
     *
     * @return The updated Mokadex after the artwork has been successfully added.
     * @throws RokaMokaContentNotFoundException If the artwork corresponding to the QR code is not found.
     * @throws RokaMokaContentDuplicatedException If the artwork has already been collected and is present in the user's
     * Mokadex.
     * @throws ServiceException If there is an internal error while adding the artwork to the Mokadex.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mokadex collectStar(@NotBlank String qrCode)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        Mokadex mokadex = this.getMokadexByLoggedUser();
        Artwork artwork = this.artworkService.getByQrCodeOrThrow(qrCode);

        if (mokadex.containsArtwork(artwork)) {
            this.sendMessageToBrokerIfReady(mokadex, artwork.getExhibition());
            throw new RokaMokaContentDuplicatedException("Obra já foi coletada");
        }
        if (!mokadex.addArtwork(artwork)) {
            throw new ServiceException("Erro ao coletar estrela na mokadex");
        }

        log.info("Estrela coletada com sucesso!");
        mokadex = this.mokadexRepository.save(mokadex);
        this.sendMessageToBrokerIfReady(mokadex, artwork.getExhibition());

        return mokadex;
    }

    private Mokadex getMokadexByLoggedUser() {
        UserAuthenticated loggedInUser = ServiceContext.getContext().getUser();
        return this.mokadexRepository
                .findMokadexByUsername(loggedInUser.getUsername())
                .orElseThrow(() -> new ServiceException("Mokadex não encontrado para usuário logado"));
    }

    private void sendMessageToBrokerIfReady(Mokadex mokadex, Exhibition exhibition) {
        CollectEmblemDTO collectEmblemDTO = new CollectEmblemDTO(mokadex.getId(), exhibition.getId());
        if (!this.emblemService.existsEmblemByExhibitionId(exhibition.getId())) {
            throw new ServiceException("Emblema não foi criado");
        }
        if (this.mokadexRepository.hasCollectedAllArtworksInExhibition(mokadex.getId(), exhibition.getId())) {
            this.rabbitTemplate.convertAndSend(this.exchangeConfigProperties.getEmblems(),
                    "",
                    collectEmblemDTO);
        }
    }

    /**
     * Tries to collect an emblem and associates it with the specified Mokadex if not already collected.
     *
     * @param mokadexId The unique identifier of the Mokadex where the emblem is to be collected.
     * @param emblem The emblem to be collected.
     *
     * @return The updated Mokadex object with the emblem added.
     * @throws RokaMokaContentNotFoundException If the Mokadex with the given ID is not found.
     * @throws RokaMokaContentDuplicatedException If the emblem is already collected in the specified Mokadex.
     * @throws ServiceException If there is an error while adding the emblem to the Mokadex.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mokadex collectEmblem(Long mokadexId, Emblem emblem)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        Mokadex mokadex = findById(mokadexId);

        if (mokadex.containsEmblem(emblem)) {
            throw new RokaMokaContentDuplicatedException("Emblema já foi coletado");
        }
        if (!mokadex.addEmblem(emblem)) {
            throw new ServiceException("Erro ao coletar emblema na mokadex");
        }

        return this.mokadexRepository.save(mokadex);
    }

    /**
     * Retrieves a set of missing artwork for a specific exhibition that is not yet present in the user's Mokadex
     * collection.
     *
     * @param exhibitionId The unique identifier of the exhibition to retrieve missing stars from.
     *
     * @return A set of {@link Artwork} objects representing the missing stars for the given exhibition in the user's
     * Mokadex.
     * @throws RokaMokaContentNotFoundException If either the user's Mokadex or the specified exhibition cannot be
     * found.
     */
    @Override
    public Set<Artwork> getMissingStarsByExhibition(@NotNull Long exhibitionId) throws RokaMokaContentNotFoundException {
        Mokadex mokadex = this.getMokadexByLoggedUser();
        Exhibition exhibition = this.exhibitionService.findById(exhibitionId);
        return this.mokadexRepository.findAllMissingStars(mokadex.getId(), exhibition.getId());
    }
}
