package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import org.apache.coyote.BadRequestException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ArtworkController} class, which is responsible for handling artwork-related endpoints.
 *
 * @author MauricioMucci
 * @see IArtworkService
 * @see ArtworkRepository
 */
@ExtendWith(MockitoExtension.class)
class ArtworkControllerTest implements ControllerResponseValidator {

    @InjectMocks private ArtworkController artworkController;

    @Mock private IArtworkService artworkService;
    @Mock private ArtworkRepository artworkRepository;

    private static ArtworkInputDTO input;
    private static Artwork artwork;
    private static ArtworkOutputDTO expected;

    @BeforeAll
    static void setUp() {
        input = mock(ArtworkInputDTO.class);
        artwork = Instancio.create(Artwork.class);
        expected = new ArtworkOutputDTO(artwork);
    }

    //region getArtworkOrElseThrow
    @Test
    void findById_shouldReturnArtworkOutputDTO_whenArtworkExistsById() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.getArtworkOrElseThrow(anyLong())).thenReturn(artwork);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService).getArtworkOrElseThrow(anyLong());
        verify(this.artworkRepository).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.getArtworkOrElseThrow(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.findById(1L));

        verify(this.artworkService).getArtworkOrElseThrow(anyLong());
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region getAllByExhibitionId
    static Stream<List<Artwork>> provideArtworkList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(Artwork.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideArtworkList")
    void getAllByExhibitionId_shouldReturnArtworkOutputDTOList_whenCalled(List<Artwork> artworks) {
        // Arrange
        when(this.artworkService.getAllArtworkByExhibitionId(anyLong())).thenReturn(artworks);

        // Act
        ResponseEntity<ApiResponseWrapper<List<ArtworkOutputDTO>>> response =
                this.artworkController.getAllByExhibitionId(1L);

        // Assert
        List<ArtworkOutputDTO> dto = artworks.stream().map(ArtworkOutputDTO::new).toList();
        this.assertListResponse(response, dto);

        verify(this.artworkService).getAllArtworkByExhibitionId(anyLong());
    }
    //endregion

    //region getArtworkByQrCode
    @Test
    void getArtworkByQrCode_shouldReturnArtworkOutputDTO_whenArtworkExistsByQrCode()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.getArtworkByQrCode("");

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.artworkRepository).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void getArtworkByQrCode_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistByQrCode()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.getArtworkByQrCode(""));

        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region uploadImage
    @Test
    void uploadImage_shouldThrowBadRequestException_whenImageIsNull() {
        assertThrows(BadRequestException.class, () -> this.artworkController.uploadImage(1L, null));
        verifyNoInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void uploadImage_shouldThrowBadRequestException_whenImageIsEmpty() {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> this.artworkController.uploadImage(1L, image));

        verifyNoMoreInteractions(this.artworkService, image);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void uploadImage_shouldReturnVoidResponse_whenSuccessful()
    throws BadRequestException, RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(false);

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.artworkController.uploadImage(1L, image);

        // Assert
        this.assertVoidResponse(response);

        verify(this.artworkService).addImage(anyLong(), any(MultipartFile.class));
        verifyNoMoreInteractions(this.artworkService, image);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void uploadImage_shouldThrowRokaMokaForbiddenException_whenImageAlreadyExists()
    throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(false);
        doThrow(RokaMokaForbiddenException.class).when(this.artworkService)
                .addImage(anyLong(), any(MultipartFile.class));

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class, () -> this.artworkController.uploadImage(1L, image));

        verify(this.artworkService).addImage(anyLong(), any(MultipartFile.class));
        verifyNoMoreInteractions(this.artworkService, image);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region register
    @Test
    void register_shouldReturnArtworkOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.create(anyLong(), any(ArtworkInputDTO.class))).thenReturn(artwork);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.register(1L, input);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService).create(anyLong(), any(ArtworkInputDTO.class));
        verifyNoMoreInteractions(this.artworkService);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void register_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.create(anyLong(), any(ArtworkInputDTO.class))).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.register(1L, input));

        verify(this.artworkService).create(anyLong(), any(ArtworkInputDTO.class));
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region patch
    @Test
    void patch_shouldReturnArtworkOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.update(input)).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.patch(input);

        // Assert
        this.assertExpectedResponse(response, expected);
        verify(this.artworkService).update(input);
    }

    @Test
    void patch_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.update(input)).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.patch(input));

        verify(this.artworkService).update(input);
    }
    //endregion

    //region remove
    @Test
    void remove_shouldReturnArtworkOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.delete(anyLong())).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.remove(1L);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService).delete(anyLong());
    }

    @Test
    void remove_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.delete(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.remove(1L));

        verify(this.artworkService).delete(anyLong());
    }
    //endregion
}
