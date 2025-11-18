package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.location.input.AddressInputDTO;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.AddressOutputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.AddressRepository;
import br.edu.ufpel.rokamoka.repository.LocationRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import org.instancio.Instancio;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link LocationService} class, which is responsible for handling location-related API operations.
 *
 * @author MauricioMucci
 * @see LocationRepository
 */
@ExtendWith(MockitoExtension.class)
class LocationServiceTest implements MockRepository<Location> {

    @InjectMocks private LocationService locationService;

    @Mock private LocationRepository locationRepository;
    @Mock private AddressRepository addressRepository;

    @Captor private ArgumentCaptor<Location> locationCaptor;

    //region getLocation
    @Test
    void getLocation_shouldReturnLocationDTO_whenLocationExistsById() {
        // Arrange
        Location location = Instancio.create(Location.class);

        when(this.locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        // Act
        LocationOutputDTO actual = this.locationService.getLocation(1L);

        // Assert
        assertNotNull(actual);

        verify(this.locationRepository).findById(anyLong());
    }

    @Test
    void getLocation_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistById() {
        // Arrange
        when(this.locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationService.getLocation(1L));

        verify(this.locationRepository).findById(anyLong());
    }
    //endregion

    //region getAllLocationsByAddress
    static Stream<List<Location>> provideLocationList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(Location.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideLocationList")
    void getAllLocationsByAddress_shouldReturnLocationOutputDTOList_whenCalled(List<Location> locations) {
        // Arrange
        when(this.locationRepository.findAllByEndereco_Id(anyLong())).thenReturn(locations);

        // Act
        List<LocationOutputDTO> actual = this.locationService.getAllLocationsByAddress(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(locations.size(), actual.size());

        verify(this.locationRepository).findAllByEndereco_Id(anyLong());
    }
    //endregion

    //region getAllLocations
    @ParameterizedTest
    @MethodSource("provideLocationList")
    void getAllLocations_shouldReturnLocationOutputDTOList_whenCalled(List<Location> locations) {
        // Arrange
        when(this.locationRepository.findAll()).thenReturn(locations);

        // Act
        List<LocationOutputDTO> actual = this.locationService.getAllLocations();

        // Assert
        assertNotNull(actual);
        assertEquals(locations.size(), actual.size());

        verify(this.locationRepository).findAll();
    }
    //endregion

    //region getAllLocations
    static Stream<List<Address>> provideAddressList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(Address.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideAddressList")
    void getAllLocations_shouldReturnAddressOutputDTOList_whenCalled(List<Address> addresses) {
        // Arrange
        when(this.addressRepository.findAll()).thenReturn(addresses);

        // Act
        List<AddressOutputDTO> actual = this.locationService.getAllAddresses();

        // Assert
        assertNotNull(actual);
        assertEquals(addresses.size(), actual.size());

        verify(this.addressRepository).findAll();
    }
    //endregion

    //region create
    static Stream<LocationInputDTO> provideCreateInput() {
        AddressInputDTO endereco = Instancio.of(AddressInputDTO.class)
                .ignore(field(AddressInputDTO::complemento))
                .create();
        LocationInputDTO locationWithComplemento = Instancio.of(LocationInputDTO.class)
                .ignore(field(LocationInputDTO::id))
                .create();
        LocationInputDTO locationWithoutComplemento = Instancio.of(LocationInputDTO.class)
                .ignore(field(LocationInputDTO::id))
                .set(field(LocationInputDTO::endereco), endereco)
                .create();
        return Stream.of(locationWithComplemento, locationWithoutComplemento);
    }

    @ParameterizedTest
    @MethodSource("provideCreateInput")
    void create_shouldReturnLocationWithOlderAddress_whenAddressAlreadyExists(LocationInputDTO input) {
        // Arrange
        Address address = mock(Address.class);

        when(this.locationRepository.existsByNome(anyString())).thenReturn(false);

        boolean isComplementoNull = input.endereco().complemento() == null;
        if (isComplementoNull) {
            when(this.addressRepository.findByRuaAndNumeroAndCep(anyString(), anyString(), anyString())).thenReturn(
                    Optional.ofNullable(address));
        } else {
            when(this.addressRepository.findByRuaAndNumeroAndCepAndComplemento(anyString(), anyString(), anyString(),
                    anyString())).thenReturn(Optional.ofNullable(address));
        }

        when(this.locationRepository.save(any(Location.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        LocationOutputDTO actual = this.locationService.create(input);

        // Assert
        verify(this.locationRepository).existsByNome(anyString());
        if (isComplementoNull) {
            verify(this.addressRepository).findByRuaAndNumeroAndCep(anyString(), anyString(), anyString());
        } else {
            verify(this.addressRepository).findByRuaAndNumeroAndCepAndComplemento(anyString(), anyString(), anyString(),
                    anyString());
        }
        verify(this.locationRepository).save(this.locationCaptor.capture());

        Location newLocation = this.locationCaptor.getValue();
        assertNotNull(actual);
        assertEquals(input.nome(), newLocation.getNome());
        assertEquals(address, newLocation.getEndereco());
    }

    @ParameterizedTest
    @MethodSource("provideCreateInput")
    void create_shouldThrowRokaMokaContentDuplicatedException_whenLocationExistsByName(LocationInputDTO input) {
        // Arrange
        when(this.locationRepository.existsByNome(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.locationService.create(input));

        verify(this.locationRepository).existsByNome(anyString());
        verifyNoMoreInteractions(this.locationRepository);
    }
    //endregion

    //region update
    static Stream<LocationInputDTO> provideUpdateInput() {
        LocationInputDTO locationIgnoreEndereco = Instancio.of(LocationInputDTO.class)
                .ignore(field(LocationInputDTO::endereco))
                .create();
        LocationInputDTO location = Instancio.create(LocationInputDTO.class);
        return Stream.of(location, locationIgnoreEndereco);
    }

    @ParameterizedTest
    @MethodSource("provideUpdateInput")
    void update_shouldReturnLocationOutputDTO_whenSuccessful(LocationInputDTO input) {
        // Arrange
        Location location = Instancio.create(Location.class);

        when(this.locationRepository.findById(input.id())).thenReturn(Optional.of(location));
        when(this.locationRepository.save(any(Location.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        LocationOutputDTO actual = this.locationService.update(input);

        // Assert
        verify(this.locationRepository).findById(anyLong());
        verify(this.locationRepository).save(this.locationCaptor.capture());

        Location updatedLocation = this.locationCaptor.getValue();
        assertNotNull(actual);
        assertEquals(input.nome(), updatedLocation.getNome());

        AddressInputDTO addressInput = input.endereco();
        if (addressInput != null) {
            Address updatedAddress = updatedLocation.getEndereco();
            assertEquals(addressInput.rua(), updatedAddress.getRua());
            assertEquals(addressInput.cep(), updatedAddress.getCep());
            assertEquals(addressInput.numero(), updatedAddress.getNumero());
            assertEquals(addressInput.complemento(), updatedAddress.getComplemento());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUpdateInput")
    void update_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistById(LocationInputDTO input) {
        // Arrange
        when(this.locationRepository.findById(input.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationService.update(input));

        verify(this.locationRepository).findById(anyLong());
        verifyNoMoreInteractions(this.locationRepository);
    }
    //endregion

    //region delete
    @Test
    void delete_shouldReturnLocationOutputDTO_whenSuccessful() {
        // Arrange
        Location location = Instancio.create(Location.class);

        when(this.locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        // Act
        LocationOutputDTO actual = this.locationService.delete(1L);

        // Assert
        assertNotNull(actual);

        verify(this.locationRepository).findById(anyLong());
        verify(this.locationRepository).delete(any(Location.class));
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistForId() {
        // Arrange
        when(this.locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationService.delete(1L));

        verify(this.locationRepository).findById(anyLong());
        verifyNoMoreInteractions(this.locationRepository);
    }
    //endregion
}
