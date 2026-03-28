package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.component.CollectEmblemProducer;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaNoUserInContextException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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

    @InjectMocks
    private MokadexService mokadexService;

    @Mock
    private MokadexRepository mokadexRepository;

    @Mock
    private EmblemService emblemService;
    @Mock
    private ArtworkService artworkService;
    @Mock
    private ExhibitionService exhibitionService;
    @Mock
    private CollectEmblemProducer collectEmblemProducer;

    static Stream<Mokadex> provideMokadex() {
        var fullMokadex = Instancio.create(Mokadex.class);
        var emptyArtwork = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getArtworks), Collections.emptySet())
                .create();
        var emptyEmblem = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getEmblems), Collections.emptySet())
                .create();
        var emptyMokadex = Instancio.of(Mokadex.class)
                .set(field(Mokadex::getArtworks), Collections.emptySet())
                .set(field(Mokadex::getEmblems), Collections.emptySet())
                .create();
        return Stream.of(fullMokadex, emptyArtwork, emptyEmblem, emptyMokadex);
    }

    static Stream<Arguments> provideGetMissingStarsByExhibitionInput() {
        var exhibition = Instancio.create(Exhibition.class);
        var emblem = Instancio
                .of(Emblem.class)
                .set(field(Emblem::getExhibition), exhibition)
                .create();
        var mokadex = Instancio
                .of(Mokadex.class)
                .set(field(Mokadex::getEmblems), new HashSet<>(Set.of(emblem)))
                .create();
        var artworks = Instancio
                .ofSet(Artwork.class)
                .set(field(Artwork::getExhibition), exhibition)
                .create();
        return Stream.of(
                Arguments.of(exhibition, mokadex, artworks),
                Arguments.of(exhibition, mokadex, Collections.emptySet()));
    }

    //region findById
    @Test
    void findById_shouldReturnMokadex_whenMokadexExistsById() {
        // Arrange
        var expected = mock(Mokadex.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        // Act
        var actual = this.mokadexService.findById(1L);

        // Assert
        verify(this.mokadexRepository).findById(anyLong());

        assertEquals(expected, actual);
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
        var user = mock(User.class);
        var expected = mock(Mokadex.class);

        when(user.getNome()).thenReturn(LOGGED_USER_NAME);
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(expected));

        // Act
        var actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verifyNoMoreInteractions(this.mokadexRepository);

        assertEquals(expected, actual);
    }

    @Test
    void getOrCreateMokadexByUser_shouldCreateMokadex_whenMokadexDoesNotExistByUser() {
        // Arrange
        var user = mock(User.class);

        when(user.getNome()).thenReturn(LOGGED_USER_NAME);
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());
        when(this.mokadexRepository.save(any(Mokadex.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        var actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.mokadexRepository).save(any(Mokadex.class));
        verifyNoMoreInteractions(this.mokadexRepository);

        assertNotNull(actual);
        assertEquals(user, actual.getUsuario());
    }
    //endregion

    //region getMokadexOutputDTOByMokadex
    @ParameterizedTest
    @MethodSource("provideMokadex")
    void getMokadexOutputDTOByMokadex_shouldReturnMokadexOutputDTO_whenCalled(Mokadex mokadex) {
        var result = this.mokadexService.getMokadexOutputDTOByMokadex(mokadex);
        assertNotNull(result);
        assertAll(
            () -> assertEquals(mokadex.getEmblems().size(), result.emblemSet().size()),
            () -> assertEquals(mokadex.getArtworks().size(), result.collectionSet().size())
        );
    }
    //endregion

    //region collectStar
    @Test
    void collectStar_shouldThrowServiceException_whenMokadexDoesNotExistForLoggedUser() {
        // Arrange
        var mockContext = this.mockServiceContext();

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class, () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verifyNoMoreInteractions(this.mokadexRepository);
    }

    @Test
    void collectStar_shouldPublishMessageAndThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsArtworkButEmblemWasNotCollected() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var mokadex = mock(Mokadex.class);
        var artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(1L);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong())).thenReturn(true);

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentDuplicatedException.class, () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.emblemService).existsEmblemByExhibitionId(anyLong());
        verify(this.mokadexRepository).hasCollectedAllArtworksInExhibition(anyLong(), anyLong());
        verify(this.collectEmblemProducer).publishCollectEmblem(any(Mokadex.class), any(Exhibition.class));
        verifyNoMoreInteractions(
                this.mokadexRepository,
                this.artworkService,
                this.emblemService,
                this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldThrowServiceException_whenMokadexAlreadyContainsArtworkButEmblemDoesNotExist() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var mokadex = mock(Mokadex.class);
        var artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(false);

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class, () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.emblemService).existsEmblemByExhibitionId(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, this.emblemService);
        verifyNoInteractions(this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsArtworkAndEmblemWasAlreadyCollected() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var mokadex = mock(Mokadex.class);
        var artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(true);
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(1L);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong())).thenReturn(false);

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentDuplicatedException.class, () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.emblemService).existsEmblemByExhibitionId(anyLong());
        verify(this.mokadexRepository).hasCollectedAllArtworksInExhibition(anyLong(), anyLong());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService, this.emblemService);
        verifyNoInteractions(this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldThrowServiceException_whenMokadexFailsToCollectStar() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var mokadex = mock(Mokadex.class);
        var artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(false);
        when(mokadex.addArtwork(artwork)).thenReturn(false);

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(ServiceException.class, () -> this.mokadexService.collectStar("QRCODE"));
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verifyNoMoreInteractions(this.mokadexRepository, this.artworkService);
        verifyNoInteractions(this.emblemService, this.collectEmblemProducer);
    }

    @Test
    void collectStar_shouldCollectStarAndUpdateMokadexAndPublishMessage_whenMokadexDoesNotContainArtwork() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var mokadex = mock(Mokadex.class);
        var artwork = Instancio.create(Artwork.class);

        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(mokadex.containsArtwork(artwork)).thenReturn(false);
        when(mokadex.addArtwork(artwork)).thenReturn(true);
        when(this.mokadexRepository.save(mokadex)).thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));
        when(this.emblemService.existsEmblemByExhibitionId(anyLong())).thenReturn(true);
        when(mokadex.getId()).thenReturn(DEFAULT_ID);
        when(this.mokadexRepository.hasCollectedAllArtworksInExhibition(anyLong(), anyLong())).thenReturn(true);

        // Act & Assert
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.mokadexService.collectStar("QRCODE");
        }

        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.emblemService).existsEmblemByExhibitionId(anyLong());
        verify(this.mokadexRepository).save(any(Mokadex.class));
        verify(this.mokadexRepository).hasCollectedAllArtworksInExhibition(anyLong(), anyLong());
        verify(this.collectEmblemProducer).publishCollectEmblem(any(Mokadex.class), any(Exhibition.class));
        verifyNoMoreInteractions(
                this.mokadexRepository,
                this.artworkService,
                this.emblemService,
                this.collectEmblemProducer);
    }
    //endregion

    //region collectEmblem
    @Test
    void collectEmblem_shouldCollectEmblemAndUpdateMokadex_whenMokadexDoesNotContainEmblem() {
        // Arrange
        var mokadex = mock(Mokadex.class);
        var emblem = mock(Emblem.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(emblem)).thenReturn(false);
        when(mokadex.addEmblem(emblem)).thenReturn(true);

        // Act & Assert
        this.mokadexService.collectEmblem(1L, emblem);

        verify(this.mokadexRepository).findById(anyLong());
        verify(this.mokadexRepository).save(mokadex);
        verifyNoMoreInteractions(this.mokadexRepository);
    }

    @Test
    void collectEmblem_shouldThrowRokaMokaContentDuplicatedException_whenMokadexAlreadyContainsEmblem() {
        // Arrange
        var mokadex = mock(Mokadex.class);
        var emblem = mock(Emblem.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(emblem)).thenReturn(true);

        // Act & Assert
        assertThrows(
                RokaMokaContentDuplicatedException.class,
                () -> this.mokadexService.collectEmblem(1L, emblem));

        verify(this.mokadexRepository).findById(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository);
    }

    @Test
    void collectEmblem_shouldThrowServiceException_whenMokadexFailsToCollectEmblem() {
        // Arrange
        var mokadex = mock(Mokadex.class);
        var emblem = mock(Emblem.class);

        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.of(mokadex));
        when(mokadex.containsEmblem(emblem)).thenReturn(false);
        when(mokadex.addEmblem(emblem)).thenReturn(false);

        // Act & Assert
        assertThrows(ServiceException.class, () -> this.mokadexService.collectEmblem(1L, emblem));

        verify(this.mokadexRepository).findById(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository);
    }

    @Test
    void collectEmblem_shouldThrowRokaMokaContentNotFoundException_whenMokadexDoesNotExistById() {
        // Arrange
        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.mokadexService.collectEmblem(1L, new Emblem()));

        verify(this.mokadexRepository).findById(anyLong());
        verifyNoMoreInteractions(this.mokadexRepository);
    }
    //endregion

    //region getMissingStarsByExhibition
    @ParameterizedTest
    @MethodSource("provideGetMissingStarsByExhibitionInput")
    void getMissingStarsByExhibition_shouldReturnSetOfArtwork_whenInputIsValid(
            Exhibition exhibition,
            Mokadex mokadex,
            Set<Artwork> expected) {
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            var mockContext = this.mockServiceContext();

            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(mokadex));
            when(this.exhibitionService.getExhibitionOrElseThrow(anyLong())).thenReturn(exhibition);
            when(this.mokadexRepository.findAllMissingStars(mokadex.getId(), exhibition.getId())).thenReturn(expected);

            var actual = this.mokadexService.getMissingStarsByExhibition(1L);

            verify(this.mokadexRepository).findMokadexByUsername(anyString());
            verify(this.exhibitionService).getExhibitionOrElseThrow(anyLong());
            verify(this.mokadexRepository).findAllMissingStars(mokadex.getId(), exhibition.getId());
            verifyNoMoreInteractions(this.mokadexRepository, this.exhibitionService);

            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
        }
    }

    @Test
    void getMissingStarsByExhibition_shouldThrowServiceException_whenMokadexDoesNotExistForLoggedUser() {
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            var mockContext = this.mockServiceContext();
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());

            assertThrows(ServiceException.class, () -> this.mokadexService.getMissingStarsByExhibition(1L));

            verify(this.mokadexRepository).findMokadexByUsername(anyString());
            verifyNoMoreInteractions(this.mokadexRepository);
            verifyNoInteractions(this.exhibitionService);
        }
    }
    //endregion

    //region getSummary
    @Test
    void getSummary_shouldThrowRokaMokaNoUserInContextException_whenNoUserInContext()
    throws RokaMokaNoUserInContextException {
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            final var mockContext = mock(ServiceContext.class);

            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            when(mockContext.getUsernameOrThrow()).thenThrow(RokaMokaNoUserInContextException.class);

            assertThrows(RokaMokaNoUserInContextException.class, () -> this.mokadexService.getSummary());

            verifyNoInteractions(this.mokadexRepository);
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void getSummary_shouldReturnMokadexSummary_whenSuccessful(long sampleCount) throws RokaMokaNoUserInContextException {
        try (var mockedServiceContext = mockStatic(ServiceContext.class)) {
            // Arrange
            var starCount = sampleCount * 2;
            var emblemCount = sampleCount * 5;
            var mockContext = mock(ServiceContext.class);

            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            when(mockContext.getUsernameOrThrow()).thenReturn(LOGGED_USER_NAME);
            when(this.mokadexRepository.getStarCount(anyString())).thenReturn(starCount);
            when(this.mokadexRepository.getEmblemCount(anyString())).thenReturn(emblemCount);

            // Act
            var summary = this.mokadexService.getSummary();

            // Assert
            verify(this.mokadexRepository).getStarCount(anyString());
            verify(this.mokadexRepository).getEmblemCount(anyString());

            assertNotNull(summary);
            assertAll(
                    () -> assertEquals(starCount, summary.starCount()),
                    () -> assertEquals(emblemCount, summary.emblemCount())
            );
        }
    }
    //endregion
}
