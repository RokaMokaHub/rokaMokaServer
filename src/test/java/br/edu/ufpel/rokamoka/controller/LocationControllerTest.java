package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.AddressOutputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
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
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link LocationController} class, which is responsible for handling location-related endpoints.
 *
 * @author MauricioMucci
 * @see ILocationService
 */
@ExtendWith(MockitoExtension.class)
class LocationControllerTest implements ControllerResponseValidator {

    @InjectMocks private LocationController locationController;

    @Mock private ILocationService locationService;

    private LocationInputDTO input;
    private LocationOutputDTO output;

    @BeforeEach
    void setUp() {
        this.input = mock(LocationInputDTO.class);
        this.output = mock(LocationOutputDTO.class);
    }

    //region getLocation
    @Test
    void getLocation_shouldReturnLocationDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.getLocation(anyLong())).thenReturn(this.output);

        // Act
        ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> response = this.locationController.getLocation(1L);

        // Assert
        this.assertExpectedResponse(response, this.output);

        verify(this.locationService).getLocation(anyLong());
    }

    @Test
    void getLocation_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.getLocation(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationController.getLocation(1L));

        verify(this.locationService).getLocation(anyLong());
    }
    //endregion

    //region getAllLocations
    static Stream<Arguments> buildLocationOutputDTOList() {
        return Stream.of(Arguments.of(Collections.emptyList()),
                Arguments.of(Instancio.ofList(LocationOutputDTO.class).create()));
    }

    @ParameterizedTest
    @MethodSource("buildLocationOutputDTOList")
    void getAllLocations_shouldReturnLocationOutputList_whenCalled(List<LocationOutputDTO> outputList) {
        // Arrange
        when(this.locationService.getAllLocations()).thenReturn(outputList);

        // Act
        ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> response =
                this.locationController.getAllLocations();

        // Assert
        verify(this.locationService).getAllLocations();

        this.assertListResponse(response, outputList);
    }
    //endregion

    //region getAllAddresses
    static Stream<Arguments> buildAddressOutputDTOList() {
        return Stream.of(Arguments.of(Collections.emptyList()),
                Arguments.of(Instancio.ofList(AddressOutputDTO.class).create()));
    }

    @ParameterizedTest
    @MethodSource("buildAddressOutputDTOList")
    void getAllAddresses_shouldReturnAddressOutputList_whenCalled(List<AddressOutputDTO> outputList) {
        // Arrange
        when(this.locationService.getAllAddresses()).thenReturn(outputList);

        // Act
        ResponseEntity<ApiResponseWrapper<List<AddressOutputDTO>>> response = this.locationController.getAllAddresses();

        // Assert
        verify(this.locationService).getAllAddresses();

        this.assertListResponse(response, outputList);
    }
    //endregion

    //region getAllLocationsByAddress
    @ParameterizedTest
    @MethodSource("buildLocationOutputDTOList")
    void getAllLocationsByAddress(List<LocationOutputDTO> outputList) {
        // Arrange
        when(this.locationService.getAllLocationsByAddress(anyLong())).thenReturn(outputList);

        // Act
        ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> response =
                this.locationController.getAllLocationsByAddress(1L);

        // Assert
        verify(this.locationService).getAllLocationsByAddress(anyLong());

        this.assertListResponse(response, outputList);
    }
    //endregion

    //region register
    @Test
    void register_shouldReturnLocationOutputDTO_whenSuccessful() throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.locationService.create(this.input)).thenReturn(this.output);

        // Act
        ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> response = this.locationController.register(this.input);

        // Assert
        this.assertExpectedResponse(response, this.output);

        verify(this.locationService).create(any(LocationInputDTO.class));
    }

    @Test
    void register_shouldThrowRokaMokaContentDuplicatedException_whenLocationAlreadyExistsByName()
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.locationService.create(this.input)).thenThrow(RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.locationController.register(this.input));

        verify(this.locationService).create(any(LocationInputDTO.class));
    }
    //endregion

    //region patch
    @Test
    void patch_shouldReturnLocationOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.update(this.input)).thenReturn(this.output);

        // Act
        ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> response = this.locationController.patch(this.input);

        // Assert
        this.assertExpectedResponse(response, this.output);

        verify(this.locationService).update(any(LocationInputDTO.class));
    }

    @Test
    void patch_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.update(this.input)).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationController.patch(this.input));

        verify(this.locationService).update(any(LocationInputDTO.class));
    }
    //endregion

    //region remove
    @Test
    void remove_shouldReturnDeletedLocationInfo_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.delete(anyLong())).thenReturn(this.output);

        // Act
        ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> response = this.locationController.remove(1L);

        // Assert
        this.assertExpectedResponse(response, this.output);

        verify(this.locationService).delete(anyLong());
    }

    @Test
    void remove_shouldThrowRokaMokaContentNotFoundException_whenLocationDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.locationService.delete(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.locationController.remove(1L));

        verify(this.locationService).delete(anyLong());
    }
    //endregion
}
