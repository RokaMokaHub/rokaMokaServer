package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code ExhibitionService} class, which is responsible for handling exhibition-related API
 * operations.
 *
 * @author MauricioMucci
 * @see ExhibitionService
 * @see ExhibitionRepository
 * @see IArtworkService
 * @see ILocationService
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionServiceTest implements MockRepository<Exhibition> {

    @InjectMocks private ExhibitionService exhibitionService;

    @Mock private ExhibitionRepository exhibitionRepository;

    @Mock private IArtworkService artworkService;
    @Mock private ILocationService locationService;

    @Captor private ArgumentCaptor<Exhibition> exhibitionCaptor;

    private Exhibition exhibition;

    @BeforeEach
    void setUp() {
        this.exhibition = Instancio.create(Exhibition.class);
    }

    //region getAllExhibitions
    static Stream<List<ExhibitionOutputDTO>> provideExhibitionOutputDTOList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(ExhibitionOutputDTO.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideExhibitionOutputDTOList")
    void getAllExhibitions_shouldReturnExhibitionList(List<ExhibitionOutputDTO> exhibitions) {
        // Arrange
        when(this.exhibitionRepository.findAllExhibitionAndCountArtworks()).thenReturn(exhibitions);

        // Act
        List<ExhibitionOutputDTO> actual = this.exhibitionService.getAllExhibitions();

        // Assert
        assertNotNull(actual);
        assertEquals(exhibitions.size(), actual.size());

        verify(this.exhibitionRepository).findAllExhibitionAndCountArtworks();
    }
    //endregion

    //region findById
    static Stream<List<Artwork>> provideArtworkList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(Artwork.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideArtworkList")
    void findById_shouldReturnExhibitionOutputDTO_whenExhibitionExistsById(List<Artwork> artworks)
    throws RokaMokaContentNotFoundException {
        // Arrange
        Long id = this.exhibition.getId();
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));
        when(this.artworkService.getAllArtworkByExhibitionId(anyLong())).thenReturn(artworks);

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.findById(id);

        // Assert
        assertExhibitionOutputByExhibition(this.exhibition, actual);

        verify(this.exhibitionRepository).findById(anyLong());
        verify(this.artworkService).getAllArtworkByExhibitionId(anyLong());
    }

    private static void assertExhibitionOutputByExhibition(Exhibition expected, ExhibitionOutputDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getName(), actual.name());
        assertEquals(expected.getDescription(), actual.description());

        List<Artwork> artworks = expected.getArtworks();
        assertEquals(artworks.size(), actual.numberOfArtworks());

        Location location = expected.getLocation();
        assertEquals(location.getNome(), actual.location());
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.findById(1L));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoInteractions(this.artworkService);
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnExhibitionOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        ExhibitionInputDTO input =
                Instancio.of(ExhibitionInputDTO.class).ignore(field(ExhibitionInputDTO::id)).create();
        Location location = mock(Location.class);

        when(this.locationService.getLocationOrElseThrow(anyLong())).thenReturn(location);
        when(this.exhibitionRepository.save(any(Exhibition.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.create(input);

        // Assert
        verify(this.locationService).getLocationOrElseThrow(anyLong());
        verify(this.exhibitionRepository).save(this.exhibitionCaptor.capture());

        Exhibition newExhibition = this.exhibitionCaptor.getValue();
        assertExhibitionOutputByExhibition(newExhibition, actual);
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExist()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ExhibitionInputDTO input = mock(ExhibitionInputDTO.class);

        when(this.locationService.getLocationOrElseThrow(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.create(input));

        verify(this.locationService).getLocationOrElseThrow(anyLong());
        verifyNoInteractions(this.exhibitionRepository);
    }
    //endregion

    //region delete
    @Test
    void delete_shouldDeleteExhibitionAndRelatedArtworksAndReturnExhibitionOutputDTO_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Long id = this.exhibition.getId();

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.delete(id);

        // Assert
        assertExhibitionOutputByExhibition(this.exhibition, actual);

        verify(this.exhibitionRepository).findById(anyLong());
        verify(this.artworkService).deleteByExhibitionId(anyLong());
        verify(this.exhibitionRepository).delete(any(Exhibition.class));
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExist() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.delete(1L));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionRepository);
        verifyNoInteractions(this.artworkService);
    }
    //endregion

    //region addArtworks
    static Stream<List<ArtworkOutputDTO>> provideArtworkOutputDTOList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(ArtworkOutputDTO.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideArtworkOutputDTOList")
    void addArtworks_shouldReturnExhibitionOutputDTO_whenSuccessful(List<ArtworkOutputDTO> artworks)
    throws RokaMokaContentNotFoundException {
        // Arrange
        Long id = this.exhibition.getId();

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));
        when(this.artworkService.addArtworksToExhibition(anyList(), any(Exhibition.class))).thenReturn(artworks);

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.addArtworks(id, Collections.emptyList());

        // Assert
        assertNotNull(actual);
        assertEquals(artworks.size(), actual.numberOfArtworks());

        verify(this.exhibitionRepository).findById(anyLong());
        verify(this.artworkService).addArtworksToExhibition(anyList(), any(Exhibition.class));
    }

    @Test
    void addArtworks_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.exhibitionService.addArtworks(1L, Collections.emptyList()));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoInteractions(this.artworkService);
    }
    //endregion

    //region getExhibitionOrElseThrow
    @Test
    void getExhibitionOrElseThrow_shouldReturnExhibition_whenExhibitionExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Long id = this.exhibition.getId();

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));

        // Act
        Exhibition actual = this.exhibitionService.getExhibitionOrElseThrow(id);

        // Assert
        assertNotNull(actual);
        assertEquals(this.exhibition, actual);

        verify(this.exhibitionRepository).findById(anyLong());
    }

    @Test
    void getExhibitionOrElseThrow_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.getExhibitionOrElseThrow(1L));

        verify(this.exhibitionRepository).findById(anyLong());
    }
    //endregion

    //region update
    @Test
    void update_shouldDoCompleteUpdateAndReturnExhibitionOutputDTO_whenLocationIsProvided()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ExhibitionInputDTO input = mock(ExhibitionInputDTO.class);
        Location location = mock(Location.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));
        when(input.locationId()).thenReturn(1L);
        when(this.locationService.getLocationOrElseThrow(anyLong())).thenReturn(location);
        when(this.exhibitionRepository.save(any(Exhibition.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.update(input);

        // Assert
        verify(this.locationService).getLocationOrElseThrow(anyLong());
        verify(this.exhibitionRepository).save(this.exhibitionCaptor.capture());

        Exhibition updatedExhibition = this.exhibitionCaptor.getValue();
        assertExhibitionOutputByExhibition(updatedExhibition, actual);
    }

    @Test
    void update_shouldDoParcialUpdateAndReturnExhibitionOutputDTO_whenNoLocationIsProvided()
    throws RokaMokaContentNotFoundException {
        // Arrange
        ExhibitionInputDTO input = mock(ExhibitionInputDTO.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(this.exhibition));
        when(input.locationId()).thenReturn(null);
        when(this.exhibitionRepository.save(any(Exhibition.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        ExhibitionOutputDTO actual = this.exhibitionService.update(input);

        // Assert
        verifyNoInteractions(this.locationService);
        verify(this.exhibitionRepository).save(this.exhibitionCaptor.capture());

        Exhibition updatedExhibition = this.exhibitionCaptor.getValue();
        assertExhibitionOutputByExhibition(updatedExhibition, actual);
    }

    @Test
    void update_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.getExhibitionOrElseThrow(1L));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionRepository);
        verifyNoInteractions(this.locationService);
    }
    //endregion
}
