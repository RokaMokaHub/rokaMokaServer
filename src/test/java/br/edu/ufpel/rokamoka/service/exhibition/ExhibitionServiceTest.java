package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.address.input.EnderecoDTO;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionWithArtworksDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.AddressRepository;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import org.instancio.Instancio;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ExhibitionService} class, which is responsible for handling
 * exhibition-related API operations.
 *
 * @author MauricioMucci
 * @see ExhibitionRepository
 * @see ArtworkRepository
 * @see AddressRepository
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionServiceTest implements MockRepository<Exhibition> {

    @InjectMocks private ExhibitionService exhibitionService;

    @Mock private ExhibitionRepository exhibitionRepository;
    @Mock private ArtworkRepository artworkRepository;
    @Mock private AddressRepository addressRepository;

    //region findAll
    static Stream<Arguments> buildFindAllInput() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Instancio.ofList(Exhibition.class).create())
        );
    }

    @ParameterizedTest
    @MethodSource("buildFindAllInput")
    void findAll_shouldReturnExhibitionList_whenCalled(List<Exhibition> exhibitions) {
        // Arrange
        when(this.exhibitionRepository.findAll()).thenReturn(exhibitions);

        // Act
        List<Exhibition> actual = this.exhibitionService.findAll();

        // Assert
        assertNotNull(actual);
        assertEquals(exhibitions.size(), actual.size());

        verify(this.exhibitionRepository).findAll();
    }
    //endregion

    //region findById
    @Test
    void findById_shouldReturnExhibition_whenExhibitionExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Exhibition exhibition = mock(Exhibition.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(exhibition));

        // Act
        Exhibition actual = this.exhibitionService.findById(1L);

        // Assert
        assertNotNull(actual);

        verify(this.exhibitionRepository).findById(anyLong());
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.exhibitionService.findById(1L));

        verify(this.exhibitionRepository).findById(anyLong());
    }
    //endregion

    //region save
    @Test
    void save_shouldReturnSavedExhibition_whenInputIsValid() {
        // Arrange
        ExhibitionInputDTO input = Instancio.create(ExhibitionInputDTO.class);
        Address address = new Address(input.enderecoDTO());

        when(this.addressRepository.save(any(Address.class))).thenReturn(address);
        when(this.exhibitionRepository.save(any(Exhibition.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        Exhibition actual = this.exhibitionService.save(input);

        // Assert
        this.assertExhibitionByInput(input, actual);

        verify(this.addressRepository).save(any(Address.class));
        verify(this.exhibitionRepository).save(any(Exhibition.class));
    }

    private void assertExhibitionByInput(ExhibitionInputDTO expected, Exhibition actual) {
        assertNotNull(actual);
        assertEquals(expected.name(), actual.getName());
        assertEquals(expected.description(), actual.getDescription());

        this.assertAddressByEnderecoDTO(expected.enderecoDTO(), actual.getAddress());
    }

    private void assertAddressByEnderecoDTO(EnderecoDTO expected, Address actual) {
        assertNotNull(actual);
        assertEquals(expected.rua(), actual.getRua());
        assertEquals(expected.numero(), actual.getNumero());
        assertEquals(expected.cep(), actual.getCep());
        assertEquals(expected.complemento(), actual.getComplemento());
    }
    //endregion

    //region deleteById
    @Test
    void deleteById_shouldInvokeRepository_whenCalled() {
        this.exhibitionService.deleteById(1L);
        verify(this.exhibitionRepository).deleteById(1L);
    }
    //endregion

    //region addArtworks
    @Test
    void addArtworks_shouldAddArtworksToExhibition_whenInputIsValid() throws RokaMokaContentNotFoundException {
        // Arrange
        List<ArtworkInputDTO> inputList = Instancio.ofList(ArtworkInputDTO.class).create();
        Exhibition foundById = Instancio.create(Exhibition.class);

        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.of(foundById));
        when(this.artworkRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ExhibitionWithArtworksDTO actual = this.exhibitionService.addArtworks(1L, inputList);

        // Assert
        assertNotNull(actual);
        assertEquals(foundById.getName(), actual.exhibitionName());
        assertEquals(inputList.size(), actual.artworkOutputDTOS().size());

        verify(this.exhibitionRepository).findById(anyLong());
        verify(this.artworkRepository).saveAll(anyList());
    }

    @Test
    void addArtworks_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById() {
        // Arrange
        when(this.exhibitionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.exhibitionService.addArtworks(1L, Collections.emptyList()));

        verify(this.exhibitionRepository).findById(anyLong());
        verifyNoMoreInteractions(this.artworkRepository);
    }
    //endregion
}
