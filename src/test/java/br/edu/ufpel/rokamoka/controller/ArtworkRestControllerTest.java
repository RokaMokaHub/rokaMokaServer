package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ArtworkRestController} class, which is responsible for handling artwork-related endpoints.
 *
 * @author MauricioMucci
 * @see IArtworkService
 * @see ArtworkRepository
 */
@ExtendWith(MockitoExtension.class)
class ArtworkRestControllerTest implements ControllerResponseValidator {

    @InjectMocks
    private ArtworkRestController artworkController;

    @Mock
    private IArtworkService artworkService;
    @Mock
    private ArtworkRepository artworkRepository;

    private ArtworkInputDTO input;
    private Artwork artwork;
    private ArtworkOutputDTO expected;

    @BeforeEach
    void setUp() {
        this.input = Instancio.create(ArtworkInputDTO.class);
        this.artwork = Instancio.create(Artwork.class);
        this.expected = new ArtworkOutputDTO(this.artwork);
    }

    @Test
    void findById_shouldReturnArtworkOutputDTO_whenArtworkExistsById() {
        // Arrange
        when(this.artworkService.getArtworkOrElseThrow(anyLong())).thenReturn(this.artwork);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(this.expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, this.expected);

        verify(this.artworkService).getArtworkOrElseThrow(anyLong());
        verify(this.artworkRepository).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void getArtworkByQrCode_shouldReturnArtworkOutputDTO_whenArtworkExistsByQrCode() {
        // Arrange
        when(this.artworkService.getByQrCodeOrThrow(anyString())).thenReturn(this.artwork);
        when(this.artworkRepository.createFullArtworkInfo(anyLong())).thenReturn(this.expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.getArtworkByQrCode("");

        // Assert
        this.assertExpectedResponse(response, this.expected);

        verify(this.artworkService).getByQrCodeOrThrow(anyString());
        verify(this.artworkRepository).createFullArtworkInfo(anyLong());
        verifyNoMoreInteractions(this.artworkService, this.artworkRepository);
    }

    @Test
    void register_shouldReturnArtworkOutputDTO_whenSuccessful() {
        // Arrange
        when(this.artworkService.create(anyLong(), any(ArtworkInputDTO.class))).thenReturn(this.artwork);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.register(1L, this.input);

        // Assert
        this.assertExpectedResponse(response, this.expected);

        verify(this.artworkService).create(anyLong(), any(ArtworkInputDTO.class));
        verifyNoMoreInteractions(this.artworkService);
        verifyNoMoreInteractions(this.artworkRepository);
    }

    @Test
    void patch_shouldReturnArtworkOutputDTO_whenSuccessful() {
        // Arrange
        when(this.artworkService.update(this.input)).thenReturn(this.expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.patch(this.input);

        // Assert
        this.assertExpectedResponse(response, this.expected);
        verify(this.artworkService).update(this.input);
    }

    @Test
    void remove_shouldReturnArtworkOutputDTO_whenSuccessful() {
        // Arrange
        when(this.artworkService.delete(anyLong())).thenReturn(this.expected);

        // Act
        ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> response = this.artworkController.remove(1L);

        // Assert
        this.assertExpectedResponse(response, this.expected);

        verify(this.artworkService).delete(anyLong());
    }
}
