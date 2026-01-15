package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.service.emblem.EmblemService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EmblemRestController} class, which is responsible for handling emblem-related endpoints.
 *
 * @author MauricioMucci
 * @see EmblemService
 */
@ExtendWith(MockitoExtension.class)
class EmblemRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private EmblemRestController emblemController;

    @Mock private EmblemService emblemService;

    private Emblem emblem;
    private EmblemInputDTO input;

    @BeforeEach
    void setUp() {
        this.emblem = Instancio.create(Emblem.class);
        this.input = mock(EmblemInputDTO.class);
    }

    //region findById
    @Test
    void findById_shouldReturnEmblemOutputDTO_whenEmblemExistsById() {
        // Arrange
        when(this.emblemService.findById(anyLong())).thenReturn(this.emblem);

        // Act
        ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> response = this.emblemController.findById(1L);

        // Assert
        this.assertExpectedResponse(response, new EmblemOutputDTO(this.emblem));

        verify(this.emblemService).findById(anyLong());
    }
    //endregion

    //region register
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
    //endregion

    //region remove
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
    //endregion
}
