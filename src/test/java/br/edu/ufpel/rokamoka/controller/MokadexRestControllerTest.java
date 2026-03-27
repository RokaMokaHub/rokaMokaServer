package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexSummaryDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaNoUserInContextException;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private MokadexRestController mokadexController;

    @Mock
    private MokadexService mokadexService;

    //region collectStar
    @Test
    void collectStar_shouldReturnMokadexOutput_whenSuccessful() {
        // Arrange
        var expectedOutput = Instancio.create(MokadexOutputDTO.class);

        when(this.mokadexService.collectStar(anyString())).thenReturn(mock(Mokadex.class));
        when(this.mokadexService.getMokadexOutputDTOByMokadex(any(Mokadex.class))).thenReturn(expectedOutput);

        // Act
        var response = this.mokadexController.collectStar("qrCode");

        // Assert
        verify(this.mokadexService, times(1)).collectStar(anyString());
        verify(this.mokadexService, times(1)).getMokadexOutputDTOByMokadex(any(Mokadex.class));

        this.assertExpectedResponse(response, expectedOutput);
    }
    //endregion

    //region findMissingStarsByExhibition
    @Test
    void findMissingStarsByExhibition_shouldReturnSetOfArtwork_whenExistMissingStarsToBeCollected() {
        // Arrange
        var artworks = Instancio.ofSet(Artwork.class).create();

        when(this.mokadexService.getMissingStarsByExhibition(anyLong())).thenReturn(artworks);

        // Act
        var response = this.mokadexController.findMissingStarsByExhibition(1L);

        // Assert
        verify(this.mokadexService, times(1)).getMissingStarsByExhibition(anyLong());

        this.assertSetResponse(response);
    }

    @Test
    void findMissingStarsByExhibition_shouldReturnEmptySet_whenAllStarsWereCollected() {
        // Arrange
        when(this.mokadexService.getMissingStarsByExhibition(anyLong())).thenReturn(Collections.emptySet());

        // Act
        var response = this.mokadexController.findMissingStarsByExhibition(1L);

        // Assert
        verify(this.mokadexService, times(1)).getMissingStarsByExhibition(anyLong());

        this.assertEmptySetResponse(response);
    }
    //endregion

    //region getSummary
    @Test
    void getSummary_shouldReturnMokadexSummaryOutputDTO_whenSuccessful() throws RokaMokaNoUserInContextException {
        // Arrange
        var summary = mock(MokadexSummaryDTO.class);

        when(this.mokadexService.getSummary()).thenReturn(summary);

        // Act
        var response = this.mokadexController.getSummary();

        // Assert
        verify(this.mokadexService, times(1)).getSummary();

        this.assertExpectedResponse(response, summary);
    }

    @Test
    void getSummary_shouldThrowRokaMokaNoUserInContextException_whenUserNotInContext() throws RokaMokaNoUserInContextException {
        // Arrange
        var summary = mock(MokadexSummaryDTO.class);

        when(this.mokadexService.getSummary()).thenThrow(RokaMokaNoUserInContextException.class);

        // Act & Assert
        assertThrows(RokaMokaNoUserInContextException.class, () -> this.mokadexController.getSummary());

        verify(this.mokadexService, times(1)).getSummary();
    }
    //endregion
}
