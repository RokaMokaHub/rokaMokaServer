package br.edu.ufpel.rokamoka.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.service.emblem.EmblemService;

/**
 * Unit tests for the {@link EmblemRestController} class, which is responsible
 * for handling emblem-related endpoints.
 *
 * @author MauricioMucci
 * @see EmblemService
 */
@ExtendWith(MockitoExtension.class)
class EmblemRestControllerTest implements ControllerResponseValidator {

    @InjectMocks
    private EmblemRestController emblemController;

    @Mock
    private EmblemService emblemService;

    private Emblem emblem;
    private EmblemInputDTO input;
    private EmblemOutputDTO emblemWithArtworks;

    @BeforeEach
    void setUp() {
        this.emblem = Instancio.create(Emblem.class);
        this.input = mock(EmblemInputDTO.class);
        var artwork = Instancio.create(Artwork.class);
        this.emblemWithArtworks = new EmblemOutputDTO(this.emblem, List.of(new ArtworkOutputDTO(artwork)));
    }

    // region findById
    @Test
    void findById_shouldReturnEmblemOutputDTO_whenEmblemExistsById() {
        // Arrange
        when(this.emblemService.findByIdWithArtworks(anyLong())).thenReturn(this.emblemWithArtworks);

        // Act
        ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> response = this.emblemController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, this.emblemWithArtworks);

        verify(this.emblemService).findByIdWithArtworks(anyLong());
    }
    // endregion

    // region findByExhibitionId
    @Test
    void findByExhibitionId_shouldReturnEmblemOutputDTO_whenEmblemExistsByExhibitionId() {
        // Arrange
        when(this.emblemService.findByExhibitionId(anyLong())).thenReturn(Optional.of(this.emblem));

        // Act
        ResponseEntity<ApiResponseWrapper<List<EmblemOutputDTO>>> response = this.emblemController
                .findByExhibitionId(1L);

        // Assert
        this.assertExpectedResponse(response, List.of(new EmblemOutputDTO(this.emblem)));

        verify(this.emblemService).findByExhibitionId(anyLong());
    }

    @Test
    void findByExhibitionId_shouldThrowException_whenEmblemDoesNotExist() {
        // Arrange
        when(this.emblemService.findByExhibitionId(anyLong()))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            this.emblemController.findByExhibitionId(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    // region register
    @Test
    void register_shouldReturnEmblemOutputDTO_whenSuccessful() {
        // Arrange
        when(this.emblemService.create(this.input)).thenReturn(this.emblem);

        // Act
        ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> response = this.emblemController.register(this.input);

        // Assert
        this.assertExpectedResponse(response, new EmblemOutputDTO(this.emblem));

        verify(this.emblemService).create(this.input);
    }
    // endregion

    // region remove
    @Test
    void remove_shouldReturnEmblemOutputDTO_whenSuccessful() {
        // Arrange
        when(this.emblemService.delete(anyLong())).thenReturn(this.emblem);

        // Act
        ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> response = this.emblemController.remove(1L);

        // Assert
        this.assertExpectedResponse(response, new EmblemOutputDTO(this.emblem));

        verify(this.emblemService).delete(anyLong());
    }
    // endregion
}
