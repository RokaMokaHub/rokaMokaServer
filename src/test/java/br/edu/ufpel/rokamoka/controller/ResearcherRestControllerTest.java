package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserInputDTO;
import br.edu.ufpel.rokamoka.service.user.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ResearcherRestController} class, which is responsible for handling researcher-related
 * endpoints.
 *
 * @author MauricioMucci
 * @see ResearcherRestController
 * @see UserService
 */
@ExtendWith(MockitoExtension.class)
class ResearcherRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private ResearcherRestController researcherController;

    @Mock private UserService userService;

    //region register
    @Test
    void register_shouldReturnTokenGeneratedForNewUser_whenSuccessful() {
        // Arrange
        AuthOutputDTO expectedOutput = Instancio.create(AuthOutputDTO.class);

        when(this.userService.createResearcher(any(UserInputDTO.class))).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<AuthOutputDTO>> response = this.researcherController.register(
                mock(UserInputDTO.class));

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.userService, times(1)).createResearcher(any(UserInputDTO.class));
    }
    //endregion
}
