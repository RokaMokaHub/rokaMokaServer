package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MokadexRestController} class, which is responsible for handling mokadex-related endpoints.
 *
 * @author MauricioMucci
 * @see MokadexService
 */
@ExtendWith(MockitoExtension.class)
class MokadexRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private MokadexRestController mokadexController;

    @Mock private MokadexService mokadexService;

    //region collectStar
    @Test
    void collectStar_shouldReturnMokadexOutput_whenSuccessful() {
        // Arrange
        MokadexOutputDTO expectedOutput = Instancio.create(MokadexOutputDTO.class);

        when(this.mokadexService.collectStar(anyString())).thenReturn(mock(Mokadex.class));
        when(this.mokadexService.getMokadexOutputDTOByMokadex(any(Mokadex.class))).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<MokadexOutputDTO>> response = this.mokadexController.collectStar("qrCode");

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.mokadexService, times(1)).collectStar(anyString());
        verify(this.mokadexService, times(1)).getMokadexOutputDTOByMokadex(any(Mokadex.class));
    }
    //endregion

    //region findMissingStarsByExhibition
    @Test
    void findMissingStarsByExhibition_shouldReturnSetOfArtwork_whenExistMissingStarsToBeCollected() {
        // Arrange
        Set<Artwork> artworks = Instancio.ofSet(Artwork.class).create();

        when(this.mokadexService.getMissingStarsByExhibition(anyLong())).thenReturn(artworks);

        // Act
        ResponseEntity<ApiResponseWrapper<Set<ArtworkOutputDTO>>> response =
                this.mokadexController.findMissingStarsByExhibition(1L);

        // Assert
        this.assertSetResponse(response);

        verify(this.mokadexService, times(1)).getMissingStarsByExhibition(anyLong());
    }

    @Test
    void findMissingStarsByExhibition_shouldReturnEmptySet_whenAllStarsWereCollected() {
        // Arrange
        when(this.mokadexService.getMissingStarsByExhibition(anyLong())).thenReturn(Collections.emptySet());

        // Act
        ResponseEntity<ApiResponseWrapper<Set<ArtworkOutputDTO>>> response =
                this.mokadexController.findMissingStarsByExhibition(1L);

        // Assert
        this.assertEmptySetResponse(response);

        verify(this.mokadexService, times(1)).getMissingStarsByExhibition(anyLong());
    }
    //endregion
}
