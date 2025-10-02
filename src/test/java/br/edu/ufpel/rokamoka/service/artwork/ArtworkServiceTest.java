package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Image;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.MockUserSession;
import br.edu.ufpel.rokamoka.service.image.IIMageService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ArtworkService} class, which is responsible for handling artwork-related API operations.
 *
 * @author MauricioMucci
 * @see ArtworkRepository
 * @see ExhibitionRepository
 * @see IIMageService
 */
@ExtendWith(MockitoExtension.class)
class ArtworkServiceTest implements MockUserSession, MockRepository<Artwork> {

    @InjectMocks private ArtworkService artworkService;

    @Mock private ArtworkRepository artworkRepository;
    @Mock private ExhibitionRepository exhibitionRepository;

    @Mock private IIMageService imageService;

    @Captor private ArgumentCaptor<Artwork> artworkCaptor;

    private Artwork artwork;

    @BeforeEach
    void setUp() {
        this.artwork = mock(Artwork.class);
    }

    //region findAll
    static Stream<Arguments> buildFindAllInput() {
        return Stream.of(Arguments.of(Collections.emptyList()), Arguments.of(Instancio.ofList(Artwork.class).create()));
    }

    @ParameterizedTest
    @MethodSource("buildFindAllInput")
    void findAll_shouldReturnListOfArtwork_whenCalled(List<Artwork> artworks) {
        // Arrange
        when(this.artworkRepository.findAll()).thenReturn(artworks);

        // Act
        List<Artwork> actual = this.artworkService.findAll();

        // Assert
        assertNotNull(actual);
        assertEquals(artworks.size(), actual.size());

        verify(this.artworkRepository).findAll();
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region findById
    @Test
    void findById_shouldReturnArtwork_whenArtworkExistsById() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.of(this.artwork));

        // Act
        Artwork actual = this.artworkService.findById(1L);

        // Assert
        assertEquals(this.artwork, actual);

        verify(this.artworkRepository).findById(anyLong());
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById() {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.findById(1L));

        verify(this.artworkRepository).findById(anyLong());
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region findByQrCode
    @Test
    void findByQrCode_shouldReturnArtwork_whenArtworkExistByQrCode() {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.of(this.artwork));

        // Act
        Optional<Artwork> actual = this.artworkService.findByQrCode("");

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(this.artworkRepository, times(1)).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }

    @Test
    void findByQrCode_shouldReturnEmpty_whenArtworkDoesNotExistByQrCode() {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Artwork> actual = this.artworkService.findByQrCode("");

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(this.artworkRepository, times(1)).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region getByQrCodeOrThrow
    @Test
    void getByQrCodeOrThrow_shouldReturnArtwork_whenArtworkExistsByQrCode() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.of(this.artwork));

        // Act
        Artwork actual = this.artworkService.getByQrCodeOrThrow("");

        // Assert
        assertEquals(this.artwork, actual);

        verify(this.artworkRepository, times(1)).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }

    @Test
    void getByQrCodeOrThrow_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistByQrCode() {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.getByQrCodeOrThrow("qrCode"));

        verify(this.artworkRepository, times(1)).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnNewArtwork_whenInputIsValid() throws RokaMokaContentNotFoundException {
        // Arrange
        Exhibition exhibition = Instancio.create(Exhibition.class);
        ArtworkInputDTO input = Instancio.create(ArtworkInputDTO.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(exhibition));
        when(this.imageService.upload(input.image())).thenReturn(Instancio.ofSet(Image.class).create());
        when(this.artworkRepository.save(any(Artwork.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        Artwork actual = this.artworkService.create(exhibition.getId(), input);

        // Assert
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(exhibition, actual.getExhibition());
        assertEquals(input.nome(), actual.getNome());
        assertEquals(input.descricao(), actual.getDescricao());
        assertEquals(input.nomeArtista(), actual.getNomeArtista());
        assertEquals(input.link(), actual.getLink());
        assertEquals(input.qrCode(), actual.getQrCode());

        verify(this.exhibitionRepository, times(1)).findById(anyLong());
        verify(this.artworkRepository, times(1)).save(any(Artwork.class));
        verifyNoMoreInteractions(this.exhibitionRepository, this.artworkRepository);
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        ArtworkInputDTO input = mock(ArtworkInputDTO.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.create(1L, input));

        verify(this.exhibitionRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionRepository);
        verifyNoInteractions(this.artworkRepository, this.imageService);
    }
    //endregion

    //region deleteById
    @Test
    void deleteById_shouldDeleteArtwork_whenCalled() {
        this.artworkService.deleteById(1L);

        verify(this.artworkRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region addImage
    @Test
    void addImage_shouldUploadImage_whenImageIsValid()
    throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        // Arrange
        Artwork artwork = Instancio.of(Artwork.class)
                .set(field(Artwork::getId), 1L)
                .set(field(Artwork::getImages), Collections.emptySet())
                .create();
        Set<Image> images = Instancio.ofSet(Image.class).create();

        when(this.artworkRepository.findByIdWithinImage(anyLong())).thenReturn(Optional.of(artwork));
        when(this.imageService.upload(any(MultipartFile.class))).thenReturn(images);
        when(this.artworkRepository.save(any(Artwork.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        this.artworkService.addImage(1L, mock(MultipartFile.class));

        // Assert
        verify(this.artworkRepository, times(1)).findByIdWithinImage(anyLong());
        verify(this.imageService, times(1)).upload(any(MultipartFile.class));
        verify(this.artworkRepository, times(1)).save(this.artworkCaptor.capture());
        verifyNoMoreInteractions(this.artworkRepository, this.imageService);
        verifyNoInteractions(this.exhibitionRepository);

        Artwork actual = this.artworkCaptor.getValue();
        assertNotNull(actual);
        assertEquals(images, actual.getImages());
    }

    @Test
    void addImage_shouldThrowRokaMokaContentNotFoundException_whenIdDoesNotExistWithinImage() {
        // Arrange
        when(this.artworkRepository.findByIdWithinImage(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.artworkService.addImage(1L, mock(MultipartFile.class)));

        verify(this.artworkRepository, times(1)).findByIdWithinImage(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }

    @Test
    void addImage_shouldThrowRokaMokaForbiddenException_whenArtworkAlreadyContainsImage() {
        // Arrange
        when(this.artworkRepository.findByIdWithinImage(anyLong())).thenReturn(Optional.of(this.artwork));
        when(this.artwork.getImages()).thenReturn(Set.of(new Image()));

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class,
                () -> this.artworkService.addImage(1L, mock(MultipartFile.class)));

        verify(this.artworkRepository, times(1)).findByIdWithinImage(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion
}
