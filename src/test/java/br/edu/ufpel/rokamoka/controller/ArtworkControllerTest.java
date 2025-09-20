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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author MauricioMucci
 */
@ExtendWith(MockitoExtension.class)
class ArtworkControllerTest implements ControllerResponseValidator {

    @InjectMocks private ArtworkController artworkController;

    @Mock private IArtworkService artworkService;
    @Mock private ArtworkRepository artworkRepository;

    //region findById
    @Test
    void findById_shouldReturnArtworkOutputDTO_whenArtworkExistsById() throws RokaMokaContentNotFoundException {
        // Arrange
        Artwork artwork = mock(Artwork.class);
        ArtworkOutputDTO expected = Instancio.create(ArtworkOutputDTO.class);

        when(this.artworkService.findById(anyLong())).thenReturn(artwork);
        when(artwork.getId()).thenReturn(1L);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService, times(1)).findById(anyLong());
        verify(artwork, times(1)).getId();
        verify(this.artworkRepository, times(1)).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, artwork, this.artworkRepository);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.findById(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.findById(1L));

        verify(this.artworkService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region getArtworkByQrCode
    @Test
    void getArtworkByQrCode_shouldReturnArtworkOutputDTO_whenArtworkExistsByQrCode()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Artwork artwork = mock(Artwork.class);
        ArtworkOutputDTO expected = Instancio.create(ArtworkOutputDTO.class);

        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(artwork);
        when(artwork.getId()).thenReturn(1L);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response =
                this.artworkController.getArtworkByQrCode("qrCode");

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
        verify(this.artworkRepository, times(1)).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, artwork, this.artworkRepository);
    }

    @Test
    void getArtworkByQrCode_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistByQrCode()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.getArtworkByQrCode("qrCode"));

        verify(this.artworkService, times(1)).getByQrCodeOrThrow(anyString());
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

        verify(this.artworkService, times(1)).addImage(anyLong(), any(MultipartFile.class));
        verifyNoMoreInteractions(this.artworkService, image);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void uploadImage_shouldThrowRokaMokaForbiddenException_whenImageAlreadyExists()
    throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(false);
        doThrow(RokaMokaForbiddenException.class)
                .when(this.artworkService)
                .addImage(anyLong(), any(MultipartFile.class));

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class, () -> this.artworkController.uploadImage(1L, image));

        verify(this.artworkService, times(1)).addImage(anyLong(), any(MultipartFile.class));
        verifyNoMoreInteractions(this.artworkService, image);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnArtworkOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        ArtworkInputDTO input = Instancio.create(ArtworkInputDTO.class);
        Artwork artwork = mock(Artwork.class);
        ArtworkOutputDTO expected = new ArtworkOutputDTO(artwork);

        when(this.artworkService.create(anyLong(), any(ArtworkInputDTO.class))).thenReturn(artwork);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.create(1L, input);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService, times(1)).create(anyLong(), any(ArtworkInputDTO.class));
        verifyNoMoreInteractions(this.artworkService);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Long exhibitionId = 1L;
        ArtworkInputDTO input = Instancio.create(ArtworkInputDTO.class);

        when(this.artworkService.create(exhibitionId, input))
                .thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.artworkController.create(exhibitionId, input));

        verify(this.artworkService, times(1)).create(exhibitionId, input);
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion

    //region deletar
    @Test
    void deletar_shouldReturnArtworkOutputDTO_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        Artwork artwork = mock(Artwork.class);
        ArtworkOutputDTO expected = new ArtworkOutputDTO(artwork);

        when(this.artworkService.findById(anyLong())).thenReturn(artwork);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.deletar(1L);

        // Assert
        this.assertExpectedResponse(response, expected);

        verify(this.artworkService, times(1)).findById(anyLong());
        verify(this.artworkService, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }

    @Test
    void deletar_shouldThrowRokaMokaContentNotFoundException_whenArtworkDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.artworkService.findById(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.artworkController.deletar(1L));

        verify(this.artworkService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.artworkService);
        verifyNoInteractions(this.artworkRepository);
    }
    //endregion
}
