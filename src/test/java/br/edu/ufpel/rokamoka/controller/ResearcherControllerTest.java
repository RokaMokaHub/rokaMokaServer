package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.service.user.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author MauricioMucci
 */
@ExtendWith(MockitoExtension.class)
class ResearcherControllerTest implements ControllerResponseValidator<UserAuthDTO> {

    @InjectMocks private ResearcherController researcherController;

    @Mock private UserService userService;

    //region register
    @Test
    void register_shouldReturnTokenGeneratedForNewUser_whenSuccessful() throws RokaMokaContentDuplicatedException {
        // Arrange
        UserAuthDTO expectedOutput = Instancio.create(UserAuthDTO.class);

        when(this.userService.createResearcher(any(UserBasicDTO.class)))
                .thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserAuthDTO>> response =
                this.researcherController.register(mock(UserBasicDTO.class));

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.userService, times(1)).createResearcher(any(UserBasicDTO.class));
    }

    @Test
    void register_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByEmailOrName()
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.userService.createResearcher(any(UserBasicDTO.class)))
                .thenThrow(RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.researcherController.register(mock(UserBasicDTO.class)));

        verify(this.userService, times(1))
                .createResearcher(any(UserBasicDTO.class));
    }
    //endregion
}
