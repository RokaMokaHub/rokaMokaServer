package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code ExhibitionRestController} class, which is responsible for handling exhibition-related
 * endpoints.
 *
 * @author MauricioMucci
 * @see ExhibitionRestController
 * @see ExhibitionService
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private ExhibitionRestController exhibitionController;

    @Mock private ExhibitionService exhibitionService;

    private static ExhibitionInputDTO input;
    private static ExhibitionOutputDTO output;

    @BeforeAll
    static void setUp() {
        input = mock(ExhibitionInputDTO.class);
        output = mock(ExhibitionOutputDTO.class);
    }

    //region getExhibition
    @Test
    void getExhibition_shouldReturnExhibitionOutput_whenExhibitionExistsById() {
        // Arrange
        when(this.exhibitionService.getExhibitionWithArtworks(anyLong())).thenReturn(output);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response = this.exhibitionController.getExhibition(1L);

        // Assert
        this.assertExpectedResponse(response, output);

        verify(this.exhibitionService).getExhibitionWithArtworks(anyLong());
    }
    //endregion

    //region getAllExhibitions
    static Stream<List<ExhibitionOutputDTO>> provideExhibitionOutputDTOList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(ExhibitionOutputDTO.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideExhibitionOutputDTOList")
    void getAllExhibitions_shouldReturnExhibitionOutputDTOList_whenCalled(List<ExhibitionOutputDTO> exhibitions) {
        // Arrange
        when(this.exhibitionService.getAllExhibitions()).thenReturn(exhibitions);

        // Act
        ResponseEntity<ApiResponseWrapper<List<ExhibitionOutputDTO>>> response =
                this.exhibitionController.getAllExhibitions();

        // Assert
        this.assertListResponse(response, exhibitions);

        verify(this.exhibitionService).getAllExhibitions();
    }
    //endregion

    //region register
    @Test
    void register_shouldReturnExhibitionOutput_whenSuccessful() {
        // Arrange
        when(this.exhibitionService.create(input)).thenReturn(output);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response = this.exhibitionController.register(input);

        // Assert
        this.assertExpectedResponse(response, output);

        verify(this.exhibitionService).create(input);
    }
    //endregion

    //region patch
    @Test
    void patch_shouldReturnExhibitionOutput_whenSuccessful() {
        // Arrange
        when(this.exhibitionService.update(input)).thenReturn(output);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response = this.exhibitionController.patch(input);

        // Assert
        this.assertExpectedResponse(response, output);

        verify(this.exhibitionService).update(input);
    }
    //endregion

    //region addArtworks
    @Test
    void addArtworks_shouldReturnExhibitionOutput_whenSuccessful() {
        // Arrange
        when(this.exhibitionService.addArtworks(anyLong(), anyList())).thenReturn(output);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response = this.exhibitionController.addArtworks(1L,
                Collections.emptyList());

        // Assert
        this.assertExpectedResponse(response, output);

        verify(this.exhibitionService).addArtworks(anyLong(), anyList());
    }
    //endregion

    //region remove
    @Test
    void remove_shouldReturnExhibitionOutput_whenSuccessful() {
        // Arrange
        when(this.exhibitionService.delete(anyLong())).thenReturn(output);

        // Act
        ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> response = this.exhibitionController.remove(1L);

        // Assert
        this.assertExpectedResponse(response, output);

        verify(this.exhibitionService).delete(anyLong());
    }
    //endregion
}
