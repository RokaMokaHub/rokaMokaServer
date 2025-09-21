package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionWithArtworksDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ExhibitionController} class, which is responsible for handling
 * exhibition-related endpoints.
 *
 * @author MauricioMucci
 * @see ExhibitionService
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionControllerTest implements ControllerResponseValidator {

    @InjectMocks private ExhibitionController exhibitionController;

    @Mock private ExhibitionService exhibitionService;

    //region findById
    @Test
    void findById_shouldReturnExhibitionOutputDTO_whenExhibitionExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Exhibition foundById = Instancio.create(Exhibition.class);

        when(this.exhibitionService.findById(anyLong())).thenReturn(foundById);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response =
                this.exhibitionController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, new ExhibitionOutputDTO(foundById));

        verify(this.exhibitionService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionService);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.findById(anyLong())).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.exhibitionController.findById(1L));

        verify(this.exhibitionService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionService);
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnNewExhibitionInfo_whenSuccessful() {
        // Arrange
        Exhibition exhibition = Instancio.create(Exhibition.class);
        when(this.exhibitionService.save(any(ExhibitionInputDTO.class))).thenReturn(exhibition);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response =
                this.exhibitionController.create(mock(ExhibitionInputDTO.class));

        // Assert
        this.assertExpectedResponse(response, new ExhibitionOutputDTO(exhibition));

        verify(this.exhibitionService, times(1)).save(any(ExhibitionInputDTO.class));
        verifyNoMoreInteractions(this.exhibitionService);
    }
    //endregion

    //region addArtworks
    @Test
    void addArtworks_shouldReturnUpdatedExhibition_whenInputIsValid()
    throws RokaMokaContentNotFoundException {
        // Arrange
        List<ArtworkInputDTO> input = Instancio.ofList(ArtworkInputDTO.class).create();
        Exhibition exhibition = Instancio.create(Exhibition.class);
        List<ArtworkOutputDTO> artworks = Instancio.ofList(ArtworkOutputDTO.class).create();

        when(this.exhibitionService.addArtworks(anyLong(), anyList())).thenReturn(
                new ExhibitionWithArtworksDTO(exhibition.getName(), artworks));

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionWithArtworksDTO>> response =
                this.exhibitionController.addArtworks(1L, input);

        // Assert
        this.assertExpectedResponse(response,
                new ExhibitionWithArtworksDTO(exhibition.getName(), artworks));

        verify(this.exhibitionService, times(1)).addArtworks(anyLong(), anyList());
        verifyNoMoreInteractions(this.exhibitionService);
    }

    @Test
    void addArtworks_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        List<ArtworkInputDTO> artworks = Instancio.ofList(ArtworkInputDTO.class).create();

        when(this.exhibitionService.addArtworks(1L, artworks)).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.exhibitionController.addArtworks(1L, artworks));

        verify(this.exhibitionService, times(1)).addArtworks(anyLong(), anyList());
        verifyNoMoreInteractions(this.exhibitionService);
    }
    //endregion

    //region delete
    @Test
    void delete_shouldDeleteExhibition_whenExhibitionExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        Exhibition exhibition = Instancio.create(Exhibition.class);
        when(this.exhibitionService.findById(anyLong())).thenReturn(exhibition);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response =
                this.exhibitionController.delete(1L);

        // Assert
        this.assertExpectedResponse(response, new ExhibitionOutputDTO(exhibition));

        verify(this.exhibitionService, times(1)).findById(anyLong());
        verify(this.exhibitionService, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(this.exhibitionService);
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenExhibitionDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.findById(anyLong())).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.exhibitionController.delete(1L));

        verify(this.exhibitionService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(this.exhibitionService);
    }
    //endregion
}
