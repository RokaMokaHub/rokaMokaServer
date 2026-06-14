package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.MockUserSession;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EmblemService} class, which is responsible for handling emblem-related API operations.
 *
 * @author MauricioMucci
 * @see EmblemRepository
 * @see IExhibitionService
 */
@ExtendWith(MockitoExtension.class)
class EmblemServiceTest implements MockRepository<Emblem>, MockUserSession {

    @InjectMocks private EmblemService emblemService;

    @Mock private EmblemRepository emblemRepository;
    @Mock private MokadexRepository mokadexRepository;
    @Mock private IArtworkService artworkService;
    @Mock private ArtworkRepository artworkRepository;
    @Mock private IExhibitionService exhibitionService;

    private Emblem expected;
    private EmblemInputDTO input;
    private Exhibition exhibition;
    private Mokadex mokadex;

    @BeforeEach
    void setUp() {
        this.expected = mock(Emblem.class);
        this.input = Instancio.create(EmblemInputDTO.class);
        this.exhibition = mock(Exhibition.class);
        this.mokadex = mock(Mokadex.class);
    }

    //region findById
    @Test
    void findById_shouldReturnEmblem_whenEmblemExistsById() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Emblem actual = this.emblemService.findById(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(this.expected, actual);

        verify(this.emblemRepository).findById(anyLong());
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById() {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.findById(1L));

        verify(this.emblemRepository).findById(anyLong());
    }
    //endregion

    //region findByIdWithArtworks
    @Test
    void findByIdWithArtworks_shouldReturnEmblemOutputDTO_whenUserHasEmblem() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var artwork1 = Instancio.of(Artwork.class).set(field(Artwork::getId), 10L).create();
        var artwork2 = Instancio.of(Artwork.class).set(field(Artwork::getId), 11L).create();
        var emblem = Instancio.of(Emblem.class)
                .set(field(Emblem::getExhibition), this.exhibition)
                .create();
        var artworkDto1 = new ArtworkOutputDTO(artwork1);
        var artworkDto2 = new ArtworkOutputDTO(artwork2);

        when(this.exhibition.getId()).thenReturn(99L);
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(emblem));
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(this.mokadex));
        when(this.mokadex.containsEmblem(emblem)).thenReturn(true);
        when(this.artworkService.getAllArtworkByExhibitionId(anyLong())).thenReturn(List.of(artwork1, artwork2));
        when(this.artworkRepository.createFullArtworkInfo(Set.of(10L, 11L)))
                .thenReturn(List.of(artworkDto2, artworkDto1));

        // Act
        EmblemOutputDTO actual;
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            actual = this.emblemService.findByIdWithArtworks(1L);
        }

        // Assert
        assertNotNull(actual);
        assertEquals(emblem.getId(), actual.id());
        assertEquals(emblem.getNome(), actual.nome());
        assertEquals(emblem.getDescricao(), actual.descricao());
        assertEquals(emblem.getExhibition().getId(), actual.exhibition().id());
        assertEquals(List.of(artworkDto1, artworkDto2), actual.artworks());

        verify(this.emblemRepository).findById(anyLong());
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.mokadex).containsEmblem(emblem);
        verify(this.artworkService).getAllArtworkByExhibitionId(anyLong());
        verify(this.artworkRepository).createFullArtworkInfo(Set.of(10L, 11L));
    }

    @Test
    void findByIdWithArtworks_shouldReturnEmblemOutputDTOWithoutArtworks_whenExhibitionHasNoArtworks() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var emblem = Instancio.of(Emblem.class)
                .set(field(Emblem::getExhibition), this.exhibition)
                .create();

        when(this.exhibition.getId()).thenReturn(99L);
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(emblem));
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(this.mokadex));
        when(this.mokadex.containsEmblem(emblem)).thenReturn(true);
        when(this.artworkService.getAllArtworkByExhibitionId(anyLong())).thenReturn(List.of());

        // Act
        EmblemOutputDTO actual;
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);
            actual = this.emblemService.findByIdWithArtworks(1L);
        }

        // Assert
        assertNotNull(actual);
        assertTrue(actual.artworks().isEmpty());

        verify(this.emblemRepository).findById(anyLong());
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.mokadex).containsEmblem(emblem);
        verify(this.artworkService).getAllArtworkByExhibitionId(anyLong());
        verifyNoInteractions(this.artworkRepository);
    }

    @Test
    void findByIdWithArtworks_shouldThrowForbidden_whenUserDoesNotHaveEmblem() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var emblem = Instancio.of(Emblem.class)
                .set(field(Emblem::getExhibition), this.exhibition)
                .create();

        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(emblem));
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.of(this.mokadex));
        when(this.mokadex.containsEmblem(emblem)).thenReturn(false);

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaForbiddenException.class, () -> this.emblemService.findByIdWithArtworks(1L));
        }

        verify(this.emblemRepository).findById(anyLong());
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verify(this.mokadex).containsEmblem(emblem);
        verifyNoInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void findByIdWithArtworks_shouldThrowForbidden_whenUserHasNoMokadex() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var emblem = Instancio.of(Emblem.class)
                .set(field(Emblem::getExhibition), this.exhibition)
                .create();

        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(emblem));
        when(this.mokadexRepository.findMokadexByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaForbiddenException.class, () -> this.emblemService.findByIdWithArtworks(1L));
        }

        verify(this.emblemRepository).findById(anyLong());
        verify(this.mokadexRepository).findMokadexByUsername(anyString());
        verifyNoInteractions(this.mokadex, this.artworkService, this.artworkRepository);
    }

    @Test
    void findByIdWithArtworks_shouldThrowForbidden_whenUserIsNotAuthenticated() {
        // Arrange
        var mockContext = this.mockServiceContext();
        var emblem = Instancio.of(Emblem.class)
                .set(field(Emblem::getExhibition), this.exhibition)
                .create();

        when(mockContext.getUser()).thenReturn(null);
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(emblem));

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaForbiddenException.class, () -> this.emblemService.findByIdWithArtworks(1L));
        }

        verify(this.emblemRepository).findById(anyLong());
        verifyNoInteractions(this.mokadexRepository, this.mokadex, this.artworkService, this.artworkRepository);
    }
    //endregion

    //region findByExhibitionId
    @Test
    void findByExhibitionId_shouldReturnEmblem_whenEmblemExistsByExhibitionId() {
        // Arrange
        when(this.emblemRepository.findEmblemByExhibitionId(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Optional<Emblem> actual = this.emblemService.findByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(this.emblemRepository).findEmblemByExhibitionId(anyLong());
    }

    @Test
    void findByExhibitionId_shouldReturnEmpty_whenEmblemDoesNotExistByExhibitionId() {
        // Arrange
        when(this.emblemRepository.findEmblemByExhibitionId(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Emblem> actual = this.emblemService.findByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(this.emblemRepository).findEmblemByExhibitionId(anyLong());
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnEmblem_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.getExhibitionOrElseThrow(anyLong())).thenReturn(this.exhibition);
        when(this.emblemRepository.save(any(Emblem.class)))
                .thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        Emblem actual = this.emblemService.create(this.input);

        // Assert
        assertNotNull(actual);
        assertEquals(this.input.nome(), actual.getNome());
        assertEquals(this.input.descricao(), actual.getDescricao());
        assertEquals(this.exhibition, actual.getExhibition());

        verify(this.exhibitionService).getExhibitionOrElseThrow(anyLong());
        verify(this.emblemRepository).save(any(Emblem.class));
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.getExhibitionOrElseThrow(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.create(this.input));

        verify(this.exhibitionService).getExhibitionOrElseThrow(anyLong());
        verifyNoInteractions(this.emblemRepository);
    }
    //endregion

    //region delete
    @Test
    void delete_shouldReturnEmblem_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Emblem actual = this.emblemService.delete(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(this.expected, actual);

        verify(this.emblemRepository).findById(anyLong());
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById() {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.delete(1L));

        verify(this.emblemRepository).findById(anyLong());
    }
    //endregion

    //region existsEmblemByExhibitionId
    @Test
    void existsEmblemByExhibitionId_shouldReturnTrue_whenExists() {
        // Arrange
        when(this.emblemRepository.existsEmblemByExhibitionId(anyLong())).thenReturn(true);

        // Act
        boolean actual = this.emblemService.existsEmblemByExhibitionId(1L);

        // Assert
        assertTrue(actual);

        verify(this.emblemRepository).existsEmblemByExhibitionId(anyLong());
    }

    @Test
    void existsEmblemByExhibitionId_shouldReturnFalse_whenDoesNotExist() {
        // Arrange
        when(this.emblemRepository.existsEmblemByExhibitionId(anyLong())).thenReturn(false);

        // Act
        boolean actual = this.emblemService.existsEmblemByExhibitionId(1L);

        // Assert
        assertFalse(actual);

        verify(this.emblemRepository).existsEmblemByExhibitionId(anyLong());
    }
    //endregion
}
