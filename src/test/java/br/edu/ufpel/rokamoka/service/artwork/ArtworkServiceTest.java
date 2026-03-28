package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Image;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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

    @InjectMocks
    private ArtworkService artworkService;

    @Mock
    private ArtworkRepository artworkRepository;
    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private IIMageService imageService;

    private Artwork artwork;

    static Stream<List<Artwork>> provideArtworkList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(Artwork.class).create());
    }

    static Stream<Arguments> provideInputAddArtworksToExhibition() {
        Exhibition exhibition = Instancio.create(Exhibition.class);
        List<ArtworkInputDTO> artworks = Instancio.ofList(ArtworkInputDTO.class).create();
        return Stream.of(
                Arguments.of(Collections.emptyList(), null), Arguments.of(Collections.emptyList(), exhibition),
                Arguments.of(artworks, null), Arguments.of(artworks, exhibition));
    }

    private static void assertArtworkOutputByArtwork(Artwork expected, ArtworkOutputDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getNome(), actual.nome());
        assertEquals(expected.getNomeArtista(), actual.nomeArtista());
        assertEquals(expected.getDescricao(), actual.descricao());
        assertEquals(expected.getQrCode(), actual.qrCode());
        assertEquals(expected.getLink(), actual.link());
    }

    @BeforeEach
    void setUp() {
        this.artwork = mock(Artwork.class);
    }

    //region findAll
    @ParameterizedTest
    @MethodSource("provideArtworkList")
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

    //region getArtworkOrElseThrow
    @Test
    void getArtworkOrElseThrow_shouldReturnArtwork_whenArtworkExistsById() {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.of(this.artwork));

        // Act
        Artwork actual = this.artworkService.getArtworkOrElseThrow(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(this.artwork, actual);

        verify(this.artworkRepository).findById(anyLong());
    }

    @Test
    void getArtworkOrElseThrow_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById() {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.getArtworkOrElseThrow(1L));

        verify(this.artworkRepository).findById(anyLong());
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

        verify(this.artworkRepository).findByQrCode(anyString());
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

        verify(this.artworkRepository).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region getByQrCodeOrThrow
    @Test
    void getByQrCodeOrThrow_shouldReturnArtwork_whenArtworkExistsByQrCode() {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.of(this.artwork));

        // Act
        Artwork actual = this.artworkService.getByQrCodeOrThrow("");

        // Assert
        assertEquals(this.artwork, actual);

        verify(this.artworkRepository).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }

    @Test
    void getByQrCodeOrThrow_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistByQrCode() {
        // Arrange
        when(this.artworkRepository.findByQrCode(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.getByQrCodeOrThrow("qrCode"));

        verify(this.artworkRepository).findByQrCode(anyString());
        verifyNoMoreInteractions(this.artworkRepository);
        verifyNoInteractions(this.exhibitionRepository, this.imageService);
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnNewArtwork_whenInputIsValid() {
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

        verify(this.exhibitionRepository).findById(anyLong());
        verify(this.artworkRepository).save(any(Artwork.class));
        verifyNoMoreInteractions(this.exhibitionRepository, this.artworkRepository);
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        ArtworkInputDTO input = mock(ArtworkInputDTO.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.create(1L, input));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionRepository);
        verifyNoInteractions(this.artworkRepository, this.imageService);
    }
    //endregion

    //region getAllArtworkByExhibitionId
    @ParameterizedTest
    @MethodSource("provideArtworkList")
    void getAllArtworkByExhibitionId_shouldReturnArtworkList_whenCalled(List<Artwork> artworks) {
        // Arrange
        when(this.artworkRepository.findByExhibition_Id(anyLong())).thenReturn(artworks);

        // Act
        List<Artwork> actual = this.artworkService.getAllArtworkByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(artworks.size(), actual.size());

        verify(this.artworkRepository).findByExhibition_Id(anyLong());
    }
    //endregion

    //region addArtworksToExhibition
    @ParameterizedTest
    @MethodSource("provideInputAddArtworksToExhibition")
    void addArtworksToExhibition(List<ArtworkInputDTO> artworks, Exhibition exhibition) {
        // Arrange
        when(this.artworkRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<ArtworkOutputDTO> actual = this.artworkService.addArtworksToExhibition(artworks, exhibition);

        // Assert
        assertNotNull(actual);
        assertEquals(artworks.size(), actual.size());

        verify(this.artworkRepository).saveAll(anyList());
    }
    //endregion

    //region deleteByExhibitionId
    @ParameterizedTest
    @MethodSource("provideArtworkList")
    void deleteByExhibitionId_shouldReturnDeletedArtworkOutputDTOList_whenCalled(List<Artwork> artworks) {
        // Arrange
        when(this.artworkRepository.findByExhibition_Id(anyLong())).thenReturn(artworks);

        // Act
        List<ArtworkOutputDTO> actual = this.artworkService.deleteByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(artworks.size(), actual.size());

        verify(this.artworkRepository).findByExhibition_Id(anyLong());
        verify(this.artworkRepository).deleteAllById(anyList());
    }
    //endregion

    //region update
    @Test
    void update_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById() {
        // Arrange
        var input = mock(ArtworkInputDTO.class);

        when(input.id()).thenReturn(1L);
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.update(input));

        verify(this.artworkRepository).findById(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void update_shouldUpdateArtworkInfoOnly_whenNoImageIsProvided() {
        // Arrange
        var input = Instancio
                .of(ArtworkInputDTO.class)
                .ignore(field(ArtworkInputDTO::image))
                .create();
        var artwork = new Artwork();
        var spyArtwork = spy(artwork);

        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.of(spyArtwork));
        when(this.imageService.upload(input.image())).thenReturn(null);

        // Act
        var actual = this.artworkService.update(input);

        // Assert
        verify(this.artworkRepository).findById(anyLong());
        verify(this.imageService).upload(input.image());
        verifyNoMoreInteractions(this.artworkRepository, this.imageService);

        assertArtworkOutputByArtwork(spyArtwork, actual);
    }

    @Test
    void update_shouldUpdateArtworkInfoAndSetImages_whenImageIsProvided() {
        // Arrange
        var input = Instancio.create(ArtworkInputDTO.class);
        var images = Instancio.createSet(Image.class);

        var artwork = new Artwork();
        var spyArtwork = spy(artwork);

        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.of(spyArtwork));
        when(this.imageService.upload(input.image())).thenReturn(images);

        // Act
        var actual = this.artworkService.update(input);

        // Assert
        verify(this.artworkRepository).findById(anyLong());
        verify(this.imageService).upload(input.image());
        verifyNoMoreInteractions(this.artworkRepository, this.imageService);

        assertArtworkOutputByArtwork(spyArtwork, actual);
        assertTrue(spyArtwork.getImages().containsAll(images));
    }
    //endregion

    //region delete
    @Test
    void delete_shouldReturnArtworkOutput_whenSuccessful() {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.of(this.artwork));

        // Act
        ArtworkOutputDTO actual = this.artworkService.delete(1L);

        // Assert
        assertArtworkOutputByArtwork(this.artwork, actual);

        verify(this.artworkRepository).findById(anyLong());
        verify(this.artworkRepository).delete(any(Artwork.class));
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById() {
        // Arrange
        when(this.artworkRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkService.delete(1L));

        verify(this.artworkRepository).findById(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
    }
    //endregion
}
