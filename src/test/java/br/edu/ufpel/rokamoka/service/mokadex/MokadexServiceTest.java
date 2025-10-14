package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.component.CollectEmblemProducer;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.MockUserSession;
import br.edu.ufpel.rokamoka.service.artwork.ArtworkService;
import br.edu.ufpel.rokamoka.service.emblem.EmblemService;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import org.hibernate.service.spi.ServiceException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MokadexService} class, which is responsible for handling mokadex-related API operations.
 *
 * @author MauricioMucci
 * @see MokadexRepository
 * @see EmblemService
 * @see ArtworkService
 * @see ExhibitionService
 * @see CollectEmblemProducer
 */
@ExtendWith(MockitoExtension.class)
class MokadexServiceTest implements MockUserSession, MockRepository<Mokadex> {

    @InjectMocks private MokadexService mokadexService;

    @Mock private MokadexRepository mokadexRepository;

    @Mock private EmblemService emblemService;
    @Mock private ArtworkService artworkService;
    @Mock private ExhibitionService exhibitionService;
    @Mock private CollectEmblemProducer collectEmblemProducer;

    //region getArtworkOrElseThrow
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void findById_shouldReturnMokadex_whenMokadexExistsById(Long id) throws RokaMokaContentNotFoundException {
        // Arrange
        Mokadex expected = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getId), id)
                .create();

        when(this.mokadexRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Mokadex actual = this.mokadexService.findById(id);

        // Assert
        assertEquals(expected, actual);

        verify(this.mokadexRepository).findById(id);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenMokadexDoesNotExistById() {
        // Arrange
        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.mokadexService.findById(1L));

        verify(this.mokadexRepository).findById(anyLong());
    }
    //endregion

    //region getOrCreateMokadexByUser
    @Test
    void getOrCreateMokadexByUser_shouldGetMokadex_whenMokadexExistsByUser() {
        // Arrange
        User user = Instancio.create(User.class);
        Mokadex expected = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getUsuario), user)
                .create();

        when(this.mokadexRepository.findMokadexByUsername(user.getNome())).thenReturn(Optional.of(expected));

        // Act
        Mokadex actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        assertEquals(expected, actual);
        assertEquals(user, actual.getUsuario());

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(user.getNome());
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.emblemService, this.artworkService, this.exhibitionService,
                this.collectEmblemProducer);
    }

    @Test
    void getOrCreateMokadexByUser_shouldCreateMokadex_whenMokadexDoesNotExistByUser() {
        // Arrange
        User user = Instancio.create(User.class);

        when(this.mokadexRepository.findMokadexByUsername(user.getNome())).thenReturn(Optional.empty());
        when(this.mokadexRepository.save(any(Mokadex.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        Mokadex actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        assertNotNull(actual);
        assertEquals(user, actual.getUsuario());

        verify(this.mokadexRepository, times(1)).save(any(Mokadex.class));
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.emblemService, this.artworkService, this.exhibitionService,
                this.collectEmblemProducer);
    }
    //endregion

    //region getMokadexOutputDTOByMokadex
    static Stream<Arguments> buildGetMokadexOutputDTOByMokadexInput() {
        Mokadex sampleMokadex = Instancio.create(Mokadex.class);
        Mokadex emptyArtwork = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getArtworks), Collections.emptySet())
                .create();
        Mokadex emptyEmblem = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getEmblems), Collections.emptySet())
                .create();
        Mokadex emptyMokadex = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getArtworks), Collections.emptySet())
                .set(field(Mokadex::getEmblems), Collections.emptySet())
                .create();
        return Stream.of(
                Arguments.of(sampleMokadex),
                Arguments.of(emptyArtwork),
                Arguments.of(emptyEmblem),
                Arguments.of(emptyMokadex)
        );
    }

    @ParameterizedTest
    @MethodSource("buildGetMokadexOutputDTOByMokadexInput")
    void getMokadexOutputDTOByMokadex_shouldBuildMokadexOutputDTO_whenCalled(Mokadex mokadex) {
        MokadexOutputDTO actual = this.mokadexService.getMokadexOutputDTOByMokadex(mokadex);
        assertNotNull(actual);

        Set<CollectionDTO> outputCollection = actual.collectionSet();
        assertNotNull(outputCollection);
        if (mokadex.getArtworks().isEmpty()) {
            assertTrue(outputCollection.isEmpty());
        } else {
            assertFalse(outputCollection.isEmpty());
        }

        Set<EmblemOutputDTO> outputEmblems = actual.emblemSet();
        assertNotNull(outputEmblems);
        if (mokadex.getEmblems().isEmpty()) {
            assertTrue(outputEmblems.isEmpty());
        } else {
            assertFalse(outputEmblems.isEmpty());
        }
    }
    //endregion

    //region collectStar
    @Test
    void collectStar_shouldThrowServiceException_whenMokadexDoesNotExistForLoggedUser() {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.exhibitionService, this.artworkService, this.emblemService,
                this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistForQRCode()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString()))
                .thenReturn(Optional.of(new Mokadex()));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentNotFoundException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService);
        verifyNoInteractions(this.exhibitionService, this.emblemService, this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldPublishMessageAndThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsArtworkButEmblemWasNotCollected()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();
        Mokadex mokadex = mock(Mokadex.class);
        Artwork artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(1L);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong())).thenReturn(true);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentDuplicatedException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(mokadex, times(1)).containsArtwork(any(Artwork.class));
        verify(this.emblemService, times(1)).existsEmblemByExhibitionId(anyLong());
        verify(mokadex, times(1)).getId();
        verify(this.mokadexRepository, times(1)).hasCollectedAllArtworksInExhibition(
                anyLong(), anyLong());
        verify(this.collectEmblemProducer, times(1)).publishCollectEmblem(
                any(Mokadex.class), any(Exhibition.class));
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, mokadex, this.emblemService,
                this.collectEmblemProducer);
        verifyNoInteractions(this.exhibitionService);
    }

    @Test
    void collectStar_shouldThrowServiceException_whenMokadexAlreadyContainsArtworkButEmblemDoesNotExist()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();
        Mokadex mokadex = mock(Mokadex.class);
        Artwork artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(false);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(mokadex, times(1)).containsArtwork(any(Artwork.class));
        verify(this.emblemService, times(1)).existsEmblemByExhibitionId(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, mokadex, this.emblemService);
        verifyNoInteractions(this.collectEmblemProducer, this.exhibitionService);
    }

    @Test
    void collectStar_shouldThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsArtworkAndEmblemWasAlreadyCollected()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();
        Mokadex mokadex = mock(Mokadex.class);
        Artwork artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(1L);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong())).thenReturn(false);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentDuplicatedException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(mokadex, times(1)).containsArtwork(any(Artwork.class));
        verify(this.emblemService, times(1)).existsEmblemByExhibitionId(anyLong());
        verify(mokadex, times(1)).getId();
        verify(this.mokadexRepository, times(1)).hasCollectedAllArtworksInExhibition(
                anyLong(), anyLong());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, mokadex, this.emblemService);
        verifyNoInteractions(this.collectEmblemProducer, this.exhibitionService);
    }

    @Test
    void collectStar_shouldThrowServiceException_whenMokadexFailsToCollectStar()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();
        Mokadex mokadex = mock(Mokadex.class);
        Artwork artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(false);
        when(mokadex.addArtwork(artwork)).thenReturn(false);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class,
                    () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(mokadex, times(1)).containsArtwork(any(Artwork.class));
        verify(mokadex, times(1)).addArtwork(any(Artwork.class));
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, mokadex);
        verifyNoInteractions(this.exhibitionService, this.emblemService, this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldCollectStarAndUpdateMokadexAndPublishMessage_whenMokadexDoesNotContainArtwork()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();
        Mokadex mokadex = mock(Mokadex.class);
        Artwork artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(false);
        when(mokadex.addArtwork(artwork)).thenReturn(true);
        when(this.mokadexRepository.save(mokadex)).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(DEFAULT_ID);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong()))
                .thenReturn(true);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.mokadexService.collectStar("QRCODE");
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(mokadex, times(1)).containsArtwork(any(Artwork.class));
        verify(mokadex, times(1)).addArtwork(any(Artwork.class));
        verify(this.emblemService, times(1)).existsEmblemByExhibitionId(anyLong());
        verify(this.mokadexRepository, times(1)).save(any(Mokadex.class));
        verify(this.mokadexRepository, times(1)).hasCollectedAllArtworksInExhibition(
                anyLong(), anyLong());
        verify(this.collectEmblemProducer, times(1)).publishCollectEmblem(
                any(Mokadex.class), any(Exhibition.class));
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, mokadex, this.emblemService,
                this.collectEmblemProducer);
        verifyNoInteractions(this.exhibitionService);
    }
    //endregion

    //region collectEmblem
    @Test
    void collectEmblem_shouldCollectEmblemAndUpdateMokadex_whenMokadexDoesNotContainEmblem()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        Mokadex mokadex = mock(Mokadex.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(any(Emblem.class))).thenReturn(false);
        when(mokadex.addEmblem(any(Emblem.class))).thenReturn(true);

        // Act & Assert
        this.mokadexService.collectEmblem(1L, new Emblem());

        verify(this.mokadexRepository, times(1)).findById(anyLong());
        verify(mokadex, times(1)).containsEmblem(any(Emblem.class));
        verify(mokadex, times(1)).addEmblem(any(Emblem.class));
        verify(this.mokadexRepository, times(1)).save(mokadex);
        verifyNoMoreInteractions(this.mokadexRepository, mokadex);
        verifyNoInteractions(this.artworkService, this.emblemService, this.exhibitionService,
                this.collectEmblemProducer);
    }

    @Test
    void collectEmblem_shouldThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsEmblem() {
        // Arrange
        Mokadex mokadex = mock(Mokadex.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(any(Emblem.class))).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.mokadexService.collectEmblem(1L, new Emblem()));

        verify(this.mokadexRepository, times(1)).findById(anyLong());
        verify(mokadex, times(1)).containsEmblem(any(Emblem.class));
        verifyNoMoreInteractions(this.mokadexRepository, mokadex);
        verifyNoInteractions(this.artworkService, this.emblemService, this.exhibitionService,
                this.collectEmblemProducer);
    }

    @Test
    void collectEmblem_shouldThrowServiceException_whenMokadexFailsToCollectEmblem() {
        // Arrange
        Emblem emblem = mock(Emblem.class);
        Mokadex mokadex = mock(Mokadex.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(any(Emblem.class))).thenReturn(false);
        when(mokadex.addEmblem(any(Emblem.class))).thenReturn(false);

        // Act & Assert
        assertThrows(ServiceException.class,
                () -> this.mokadexService.collectEmblem(1L, emblem));

        verify(this.mokadexRepository, times(1)).findById(anyLong());
        verify(mokadex, times(1)).containsEmblem(any(Emblem.class));
        verify(mokadex, times(1)).addEmblem(any(Emblem.class));
        verifyNoMoreInteractions(this.mokadexRepository, mokadex);
        verifyNoInteractions(this.artworkService, this.emblemService, this.exhibitionService,
                this.collectEmblemProducer);
    }

    @Test
    void collectEmblem_shouldThrowRokaMokaContentNotFoundException_whenMokadexDoesNotExistById() {
        // Arrange
        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.mokadexService.collectEmblem(1L, new Emblem()));

        verify(this.mokadexRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.artworkService, this.emblemService, this.exhibitionService,
                this.collectEmblemProducer);
    }
    //endregion

    //region getMissingStarsByExhibition
    static Stream<Arguments> buildGetMissingStarsByExhibitionInput() {
        Exhibition exhibition = Instancio.create(Exhibition.class);
        Mokadex mokadex = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getEmblems), new HashSet<>(Set.of(exhibition)))
                .create();
        Set<Artwork> artworks = Instancio.ofSet(Artwork.class)
                .set(field(Artwork::getExhibition), exhibition)
                .create();
        return Stream.of(
                Arguments.of(exhibition, mokadex, artworks),
                Arguments.of(exhibition, mokadex, Collections.emptySet())
        );
    }

    @ParameterizedTest
    @MethodSource("buildGetMissingStarsByExhibitionInput")
    void getMissingStarsByExhibition_shouldReturnSetOfArtwork_whenInputIsValid(Exhibition exhibition, Mokadex mokadex,
            Set<Artwork> expected) throws RokaMokaContentNotFoundException {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.exhibitionService.getExhibitionOrElseThrow(anyLong())).thenReturn(exhibition);
        when(this.mokadexRepository.findAllMissingStars(mokadex.getId(), exhibition.getId())).thenReturn(expected);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            Set<Artwork> actual = this.mokadexService.getMissingStarsByExhibition(1L);

            assertNotNull(actual);
            if (expected.isEmpty()) {
                assertTrue(actual.isEmpty());
            } else {
                assertFalse(actual.isEmpty());
            }
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.exhibitionService, times(1)).getExhibitionOrElseThrow(anyLong());
        verify(this.mokadexRepository, times(1)).findAllMissingStars(
                mokadex.getId(), exhibition.getId());
        verifyNoMoreInteractions(this.mokadexRepository, this.exhibitionService);
        verifyNoInteractions(this.artworkService, this.emblemService, this.collectEmblemProducer);
    }

    @Test
    void getMissingStarsByExhibition_shouldThrowServiceException_whenMokadexDoesNotExistForLoggedUser() {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class,
                    () -> this.mokadexService.getMissingStarsByExhibition(1L));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.exhibitionService, this.artworkService, this.emblemService,
                this.collectEmblemProducer);
    }

    @Test
    void getMissingStarsByExhibition_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Mokadex mokadex = Instancio.create(Mokadex.class);
        ServiceContext mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.exhibitionService.getExhibitionOrElseThrow(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentNotFoundException.class,
                    () -> this.mokadexService.getMissingStarsByExhibition(1L));
        }

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(anyString());
        verify(this.exhibitionService, times(1)).getExhibitionOrElseThrow(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository, this.exhibitionService);
        verifyNoInteractions(this.artworkService, this.emblemService, this.collectEmblemProducer);
    }
    //endregion
}
